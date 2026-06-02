package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.usecases.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class GamePhase {
    LOADING,
    SHOW_NEXT_TEAM_POPUP,
    GUESSING,
    SHOW_RESULT,
    GAME_OVER
}

@OptIn(ExperimentalUuidApi::class)
data class GameState(
    val game: Game? = null,
    val currentTrack: Track? = null,
    val isGuessCorrect: Boolean? = null,
    val currentPhase: GamePhase = GamePhase.LOADING
) {
    val currentTeam = game?.currentTeam
    val timeline: List<Track> = game?.collectedCardsByTeam?.get(currentTeam) ?: emptyList()
    val currentCardCount: Int = timeline.size
}

@OptIn(ExperimentalUuidApi::class)
class GameViewModel(
    private val getPlayableTrackUseCase: GetPlayableTrackUseCase,
    private val setupInitialGameUseCase: SetupInitialGameUseCase,
    private val loadGameSessionUseCase: LoadGameSessionUseCase,
    private val checkGuessPositionUseCase: CheckGuessPositionUseCase,
    private val processCorrectGuessUseCase: ProcessCorrectGuessUseCase,
    private val advanceTurnUseCase: AdvanceTurnUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val getGameUseCase: GetGameUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val getChronobeatPlaylistsUseCase: GetChronobeatPlaylistsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val allTeams = listOf(
        Team(Uuid.parse("00000000-0000-0000-0000-000000000001"), "Team 1", TeamColor.entries.getOrElse(0) { TeamColor.entries.first() }),
        Team(Uuid.parse("00000000-0000-0000-0000-000000000002"), "Team 2", TeamColor.entries.getOrElse(1) { TeamColor.entries.first() }),
        Team(Uuid.parse("00000000-0000-0000-0000-000000000003"), "Team 3", TeamColor.entries.getOrElse(2) { TeamColor.entries.first() }),
        Team(Uuid.parse("00000000-0000-0000-0000-000000000004"), "Team 4", TeamColor.entries.getOrElse(3) { TeamColor.entries.first() })
    )

    private var trackIdPool: MutableList<String> = mutableListOf()
    private val globalTrackCache = mutableMapOf<String, Track>()

    init {
        loadAndInitializeEngine()
    }

    private fun loadAndInitializeEngine() {
        viewModelScope.launch {
            _state.update { it.copy(currentPhase = GamePhase.LOADING) }
            try {
                val playlists = getChronobeatPlaylistsUseCase()
                if (playlists.isEmpty()) return@launch

                val playlist = playlists.first()
                trackIdPool = playlist.trackIds.shuffled().toMutableList()

                val activeDto = getGameUseCase()
                val finalDto = if (activeDto != null && activeDto.playlistId == playlist.id) {
                    val loaded = loadGameSessionUseCase(activeDto)
                    globalTrackCache.putAll(loaded)
                    activeDto
                } else {
                    setupInitialGameUseCase(playlist.id, allTeams, trackIdPool)
                }

                if (finalDto != null) {
                    val freshLoaded = loadGameSessionUseCase(finalDto)
                    globalTrackCache.putAll(freshLoaded)

                    val initialGame = mapDtoToGame(finalDto)
                    _state.update {
                        it.copy(
                            game = initialGame,
                            currentTrack = initialGame.currentTrack,
                            currentPhase = GamePhase.SHOW_NEXT_TEAM_POPUP
                        )
                    }
                }
            } catch (e: Exception) {
                println("GameViewModel: Initialization fail: ${e.message}")
            }
        }
    }

    private fun mapDtoToGame(dto: GameDto): Game {
        val teams = dto.teamIds.map { id -> allTeams.first { it.id == id } }
        val currentTeam = allTeams.first { it.id == dto.currentTeamId }
        val currentTrack = globalTrackCache[dto.currentTrackId] ?: Track(dto.currentTrackId, "Unknown","Unknown", emptyList(), 0, false)
        val winnerTeam = dto.winnerTeamId?.let { id -> allTeams.find { it.id == id } }

        val collectedCards = dto.collectedCardIdsByTeamId.map { (teamId, trackIds) ->
            val team = allTeams.first { it.id == teamId }
            val tracks = trackIds.mapNotNull { globalTrackCache[it] }
            team to tracks
        }.toMap()

        return Game(
            id = dto.id,
            teams = teams,
            currentTeam = currentTeam,
            currentTrack = currentTrack,
            collectedCardsByTeam = collectedCards,
            playlistId = dto.playlistId,
            winnerTeam = winnerTeam
        )
    }

    fun onPopupAcknowledgePressed() {
        if (_state.value.currentPhase != GamePhase.SHOW_NEXT_TEAM_POPUP) return
        _state.update { it.copy(currentPhase = GamePhase.GUESSING) }
        _state.value.currentTrack?.id?.let { playMusic(it) }
    }

    fun onGuessPressed(position: Int) {
        if (_state.value.currentPhase != GamePhase.GUESSING) return

        viewModelScope.launch {
            val currentState = _state.value
            val currentTrack = currentState.currentTrack ?: return@launch
            val timeline = currentState.timeline
            val currentDto = getGameUseCase() ?: return@launch

            val isCorrect = checkGuessPositionUseCase(timeline, currentTrack, position)

            _state.update {
                it.copy(
                    isGuessCorrect = isCorrect,
                    currentPhase = GamePhase.SHOW_RESULT
                )
            }

            delay(2000)

            var nextTrackId = currentDto.currentTrackId
            var winnerId: Uuid? = null
            var newCollectedCards = currentDto.collectedCardIdsByTeamId
            var nextTeamId = currentDto.currentTeamId

            if (isCorrect) {
                val result = processCorrectGuessUseCase(currentDto, currentTrack.id, position)
                newCollectedCards = result.first
                winnerId = result.second
            }

            if (winnerId == null) {
                val nextTrack = getPlayableTrackUseCase(trackIdPool)
                if (nextTrack != null) {
                    globalTrackCache[nextTrack.id] = nextTrack
                    nextTrackId = nextTrack.id
                }
                nextTeamId = Uuid.parse(advanceTurnUseCase(currentDto))
            }

            val updatedDto = currentDto.copy(
                collectedCardIdsByTeamId = newCollectedCards,
                currentTrackId = nextTrackId,
                winnerTeamId = winnerId,
                currentTeamId = nextTeamId
            )

            saveGameUseCase(updatedDto)

            if (winnerId != null) {
                _state.update { it.copy(currentPhase = GamePhase.GAME_OVER) }
            } else {
                val nextGameModel = mapDtoToGame(updatedDto)
                _state.update {
                    it.copy(
                        game = nextGameModel,
                        currentTrack = nextGameModel.currentTrack,
                        isGuessCorrect = null,
                        currentPhase = GamePhase.SHOW_NEXT_TEAM_POPUP
                    )
                }
            }
        }
    }

    private fun playMusic(trackId: String) {
        viewModelScope.launch {
            runCatching { playMusicUseCase(trackId) }
        }
    }
}