package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.usecases.game.AdvanceTurnUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.CheckGuessPositionUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.GetGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.ProcessCorrectGuessUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SaveGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SetupInitialGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetChronobeatPlaylistsUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetPlayableTrackUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

data class GameSnapshot(
    val team: Team? = null,
    val timeline: List<Track> = emptyList(),
    val cardCount: Int = 0,
    val track: Track? = null,
    val isCorrect: Boolean? = null
)
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
    private val checkGuessPositionUseCase: CheckGuessPositionUseCase,
    private val processCorrectGuessUseCase: ProcessCorrectGuessUseCase,
    private val advanceTurnUseCase: AdvanceTurnUseCase,
    private val playMusicUseCase: PlayMusicUseCase,
    private val getGameUseCase: GetGameUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val getChronobeatPlaylistsUseCase: GetChronobeatPlaylistsUseCase,
    private val getTeamsUseCase: GetTeamsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var trackIdPool: MutableList<String> = mutableListOf()

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

                var activeGame = getGameUseCase()

                if (activeGame == null || activeGame.playlistId != playlist.id) {
                    val allTeams = getTeamsUseCase()
                    if (allTeams.isEmpty()) {
                        println("GameViewModel: Nincsenek csapatok!")
                        return@launch
                    }

                    activeGame = setupInitialGameUseCase(playlist.id, allTeams, trackIdPool)
                }

                if (activeGame != null) {
                    _state.update {
                        it.copy(
                            game = activeGame,
                            currentTrack = activeGame.currentTrack,
                            currentPhase = GamePhase.SHOW_NEXT_TEAM_POPUP
                        )
                    }
                }
            } catch (e: Exception) {
                println("GameViewModel: Initialization fail: ${e.message}")
            }
        }
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
            val currentGame = currentState.game ?: return@launch
            val currentTrack = currentState.currentTrack ?: return@launch
            val timeline = currentState.timeline

            val isCorrect = checkGuessPositionUseCase(timeline, currentTrack, position)

            _state.update {
                it.copy(
                    isGuessCorrect = isCorrect,
                    currentPhase = GamePhase.SHOW_RESULT
                )
            }

            delay(2000)

            var updatedGame = currentGame

            if (isCorrect) {
                updatedGame = processCorrectGuessUseCase(updatedGame, currentTrack, position)
            }

            if (updatedGame.winnerTeam == null) {
                val nextTrack = getPlayableTrackUseCase(trackIdPool)
                updatedGame = advanceTurnUseCase(updatedGame, nextTrack)
            }

            saveGameUseCase(updatedGame)

            if (updatedGame.winnerTeam != null) {
                _state.update { it.copy(game = updatedGame, currentPhase = GamePhase.GAME_OVER) }
            } else {
                _state.update {
                    it.copy(
                        game = updatedGame,
                        currentTrack = updatedGame.currentTrack,
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