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
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.GetSavedGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.RestartGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.SaveGameProgressUseCase
import kotlin.uuid.Uuid

enum class GamePhase {
    SHOW_NEXT_TEAM_POPUP,
    GUESSING,
    SHOW_RESULT,
    GAME_OVER
}

@OptIn(ExperimentalUuidApi::class)
data class GameState(
    val game: Game? = null,
    val tracks: List<Track> = emptyList(),
    val currentTrack: Track? = null,
    val isGuessCorrect: Boolean? = null,
    val currentPhase: GamePhase = GamePhase.SHOW_NEXT_TEAM_POPUP
) {
    val currentTeam = game?.currentTeam
    val timeline: List<Track> = game?.collectedCardsByTeam?.get(currentTeam) ?: emptyList()
    val currentCardCount: Int = timeline.size
    val isGameWon: Boolean = game?.winnerTeam != null
}

@OptIn(ExperimentalUuidApi::class)
class GameViewModel(
    private val activeGameRepository: ActiveGameRepository,
    private val musicRepository: MusicRepository,
    private val playMusicUseCase: PlayMusicUseCase,
    private val getSavedGameUseCase: GetSavedGameUseCase,
    private val saveGameProgressUseCase: SaveGameProgressUseCase,
    private val resetGameUseCase: RestartGameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val allTeams = listOf(
        Team(
            Uuid.parse("00000000-0000-0000-0000-000000000001"),
            "Team 1",
            TeamColor.entries.getOrElse(0) { TeamColor.entries.first() }),
        Team(
            Uuid.parse("00000000-0000-0000-0000-000000000002"),
            "Team 2",
            TeamColor.entries.getOrElse(1) { TeamColor.entries.first() }),
        Team(
            Uuid.parse("00000000-0000-0000-0000-000000000003"),
            "Team 3",
            TeamColor.entries.getOrElse(2) { TeamColor.entries.first() }),
        Team(
            Uuid.parse("00000000-0000-0000-0000-000000000004"),
            "Team 4",
            TeamColor.entries.getOrElse(3) { TeamColor.entries.first() })
    )

    private var trackIdPool: MutableList<String> = mutableListOf()
    private var cachedTracks: MutableList<Track> = mutableListOf()

    init {
        loadSavedGameOrStartNew()

        viewModelScope.launch {
            activeGameRepository.observeGame().collect { dto ->
                if (dto != null) {
                    try {
                        val mappedGame = mapDtoToGame(dto)
                        _state.update { oldState ->
                            oldState.copy(
                                game = mappedGame,
                                currentTrack = mappedGame.currentTrack
                            )
                        }
                    } catch (e: Exception) {
                        println("GameViewModel: Error while making game: ${e.message}")
                    }
                }
            }
        }
    }

    private suspend fun getNextPlayableTrack(): Track? {
        while (trackIdPool.isNotEmpty()) {
            val nextId = trackIdPool.removeAt(0)
            try {
                val track = musicRepository.getTrackInfo(nextId)
                if (track.isPlayable) {
                    cachedTracks.add(track)
                    _state.update { it.copy(tracks = cachedTracks.toList()) }
                    println("GameViewModel: ${track.mainArtist} - ${track.title} downloaded!")
                    return track
                }
            } catch (e: Exception) {
                println("GameViewModel: Error when downloading $nextId  music: ${e.cause?.message ?: e.message}")
            }
        }
        return null
    }

    fun loadSavedGameOrStartNew() {
        viewModelScope.launch {
            try {
                cachedTracks.clear()
                println("GameViewModel: Getting ChronoBeat playlist...")
                val chronoBeatPlaylists = musicRepository.getChronobeatPlaylists()
                if (chronoBeatPlaylists.isEmpty()) {
                    println(" GameViewModel - Error: There is no available ChronoBeat playlist!")
                    return@launch
                }

                val selectedPlaylist = chronoBeatPlaylists[0]
                trackIdPool = selectedPlaylist.trackIds.shuffled().toMutableList()

                val savedGame = getSavedGameUseCase()
                if (savedGame != null) {
                    println("GameViewModel: Downloading saved tracks for existing game...")
                    val neededIds = savedGame.collectedCardIdsByTeamId.values.flatten() + savedGame.currentTrackId
                    neededIds.forEach { id ->
                        try {
                            val track = musicRepository.getTrackInfo(id)
                            if (cachedTracks.none { it.id == track.id }) {
                                cachedTracks.add(track)
                            }
                        } catch (e: Exception) {
                            println("GameViewModel: Error downloading track: ${e.message}")
                        }
                    }
                    _state.update { it.copy(tracks = cachedTracks.toList()) }

                    val mappedGame = mapDtoToGame(savedGame)
                    _state.update { oldState ->
                        oldState.copy(
                            game = mappedGame,
                            currentTrack = mappedGame.currentTrack
                        )
                    }
                    println("GameViewModel: Saved game successfully resumed!")
                } else {
                    loadRealMusicAndInitGame()
                }
            } catch (e: Exception) {
                println("GameViewModel: Error during loading saved game: ${e.message}")
            }
        }
    }

    fun loadRealMusicAndInitGame() {
        viewModelScope.launch {
            try {
                resetGameUseCase()
                cachedTracks.clear()

                println("GameViewModel: Getting ChronoBeat playlists...")
                val chronobeatPlaylists = musicRepository.getChronobeatPlaylists()
                if (chronobeatPlaylists.isEmpty()) {
                    println("GameViewModel: Error - There is no available ChronoBeat playlist! Cannot start the game.")
                    return@launch
                }

                val selectedPlaylist = chronobeatPlaylists[0]
                trackIdPool = selectedPlaylist.trackIds.shuffled().toMutableList()

                val existingGame = activeGameRepository.getGame()
                if (existingGame != null && existingGame.playlistId == selectedPlaylist.id) {
                    println("GameViewModel: Load current game...")
                    val neededIds = existingGame.collectedCardIdsByTeamId.values.flatten() + existingGame.currentTrackId
                    neededIds.forEach { id ->
                        try {
                            val track = musicRepository.getTrackInfo(id)
                            if (cachedTracks.none { it.id == track.id }) {
                                cachedTracks.add(track)
                            }
                        } catch (e: Exception) {
                            println("GameViewModel: Error: ${e.message}")
                        }
                    }
                    _state.update { it.copy(tracks = cachedTracks.toList()) }

                    val mappedGame = mapDtoToGame(existingGame)
                    _state.update { oldState ->
                        oldState.copy(
                            game = mappedGame,
                            currentTrack = mappedGame.currentTrack
                        )
                    }
                } else {
                    println("GameViewModel: Starting new game. Downloading started cards...")

                    val updatedMap = mutableMapOf<Uuid, List<String>>()

                    allTeams.forEach { team ->
                        val starterTrack = getNextPlayableTrack()
                        if (starterTrack != null) {
                            updatedMap[team.id] = listOf(starterTrack.id)
                        }
                    }

                    val firstCurrentTrack = getNextPlayableTrack()
                    if (firstCurrentTrack == null) {
                        println("GameViewModel: Error: Not enough music in playlist!")
                        return@launch
                    }

                    val initialDto = GameDto(
                        id = Uuid.random(),
                        teamIds = allTeams.map { it.id },
                        currentTeamId = allTeams.first().id,
                        currentTrackId = firstCurrentTrack.id,
                        collectedCardIdsByTeamId = updatedMap,
                        playlistId = selectedPlaylist.id,
                        winnerTeamId = null
                    )

                    saveGameProgressUseCase(initialDto)
                }

            } catch (e: Exception) {
                println("GameViewModel: Error while initialize: ${e.message}")
            }
        }
    }

    private fun mapDtoToGame(dto: GameDto): Game {
        val teams = dto.teamIds.mapNotNull { id -> allTeams.find { it.id == id } }
        val currentTeam = allTeams.first { it.id == dto.currentTeamId }
        val currentTrack = cachedTracks.firstOrNull { it.id == dto.currentTrackId }
            ?: throw IllegalStateException("Track not loaded yet")
        val winnerTeam = dto.winnerTeamId?.let { id -> allTeams.find { it.id == id } }

        val collectedCards = dto.collectedCardIdsByTeamId.mapNotNull { (teamId, trackIds) ->
            val team = allTeams.find { it.id == teamId } ?: return@mapNotNull null
            val tracks = trackIds.mapNotNull { trackId -> cachedTracks.find { it.id == trackId } }
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
        _state.update { it.copy(currentPhase = GamePhase.GUESSING) }

        val trackId = _state.value.currentTrack?.id
        if (trackId != null) {
            playMusic(trackId)
        }
    }

    fun onGuessPressed(position: Int) {
        if (_state.value.currentPhase != GamePhase.GUESSING) return

        viewModelScope.launch {
            validateGuess(position)
        }
    }

    private suspend fun validateGuess(position: Int) {
        val currentState = _state.value
        val currentTrack = currentState.currentTrack ?: return
        val timeline = currentState.timeline
        val currentDto = activeGameRepository.getGame() ?: return

        val isCorrect = isPositionCorrect(timeline, currentTrack, position)

        var nextTrackId = currentDto.currentTrackId
        var winnerId: Uuid? = null
        var newCollectedCards = currentDto.collectedCardIdsByTeamId

        _state.update {
            it.copy(
                isGuessCorrect = isCorrect,
                currentPhase = GamePhase.SHOW_RESULT
            )
        }

        if (isCorrect) {
            val currentTrackIds = currentDto.collectedCardIdsByTeamId[currentDto.currentTeamId] ?: emptyList()
            val updatedTrackIds = currentTrackIds.toMutableList().apply { add(position, currentTrack.id) }

            newCollectedCards = currentDto.collectedCardIdsByTeamId.toMutableMap().apply {
                put(currentDto.currentTeamId, updatedTrackIds)
            }

            if (updatedTrackIds.size >= GameConstants.CARDS_TO_WIN) {
                winnerId = currentDto.currentTeamId
            }
        }

        delay(2000)


        if (winnerId != null) {
            val updatedDto = currentDto.copy(
                collectedCardIdsByTeamId = newCollectedCards,
                currentTrackId = currentDto.currentTrackId,
                winnerTeamId = winnerId,
                currentTeamId = currentDto.currentTeamId
            )

            saveGameProgressUseCase(updatedDto)

            _state.update {
                it.copy(currentPhase = GamePhase.GAME_OVER)
            }

            return

        }

        var nextTeamId = currentDto.currentTeamId
        if (winnerId == null) {
            val nextTrack = getNextPlayableTrack()
            nextTrackId = nextTrack?.id ?: currentDto.currentTrackId

            val currentIndex = currentDto.teamIds.indexOf(currentDto.currentTeamId)
            nextTeamId = currentDto.teamIds[(currentIndex + 1) % currentDto.teamIds.size]
        }

        val updatedDto = currentDto.copy(
            collectedCardIdsByTeamId = newCollectedCards,
            currentTrackId = nextTrackId,
            winnerTeamId = winnerId,
            currentTeamId = nextTeamId
        )
        saveGameProgressUseCase(updatedDto)

        if (winnerId != null) {
            _state.update { it.copy(currentPhase = GamePhase.GAME_OVER) }
        } else {
            _state.update {
                it.copy(
                    isGuessCorrect = null,
                    currentPhase = GamePhase.SHOW_NEXT_TEAM_POPUP
                )
            }
        }
    }

    private fun isPositionCorrect(timeline: List<Track>, track: Track, position: Int): Boolean {
        if (position < 0 || position > timeline.size) return false

        val before = if (position > 0) timeline.getOrNull(position - 1) else null
        val after = if (position < timeline.size) timeline.getOrNull(position) else null

        return (before == null || before.releaseYear <= track.releaseYear) &&
                (after == null || after.releaseYear >= track.releaseYear)
    }

    private fun playMusic(trackId: String) {
        viewModelScope.launch {
            try {
                playMusicUseCase(trackId)
            } catch (e: Exception) {
                println("Error while playing: ${e.message}")
            }
        }
    }
}