package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import com.rdisoftware.chronobeat.presentation.preview.MockGameData
import com.rdisoftware.chronobeat.presentation.preview.MockMusicData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.GetSavedGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.RestartGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.SaveGameProgressUseCase

data class GameState(
    val game: Game? = null, // TODO: Replace with getGameUseCase()
    val tracks: List<Track> = emptyList(), // TODO: Replace with getTracksUseCase()
    val currentTrack: Track? = null,
    val isGuessCorrect: Boolean? = null,        // TODO: popup trigger
) {
    val currentTeam = game?.currentTeam
    val timeline: List<Track> = game?.collectedCardsByTeam
        ?.get(currentTeam) ?: emptyList()
    val currentCardCount: Int = timeline.size
    val isGameWon: Boolean = game?.collectedCardsByTeam
        ?.any { (_, tracks) -> tracks.size >= GameConstants.CARDS_TO_WIN } ?: false
}

@OptIn(ExperimentalUuidApi::class)
class GameViewModel(
    // TODO: Inject usecases here
    // private val getGameUseCase: GetGameUseCase,
    // private val getTracksUseCase: GetTracksUseCase,
    // private val validateGuessUseCase: ValidateGuessUseCase,
    // private val getNextTrackUseCase: GetNextTrackUseCase,
    // private val nextTeamUseCase: NextTeamUseCase,
    // private val addTrackToTimelineUseCase: AddTrackToTimelineUseCase
    private val playMusicUseCase: PlayMusicUseCase,
    private val getSavedGameUseCase: GetSavedGameUseCase,
    private val saveGameProgressUseCase: SaveGameProgressUseCase,
    private val resetGameUseCase: RestartGameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    init {
        loadFakeData()
    }

    fun onGuessPressed(position: Int) {
        viewModelScope.launch {
            // TODO: Add popup trigger here (separate ticket)
            // TODO: Replace with validateGuessUseCase()
            validateGuess(position)
        }
    }

    // TODO: dismissPopup

    private fun loadFakeData() {
        viewModelScope.launch {
            // TODO: Replace with:
            // val game = getGameUseCase()
            // val tracks = getTracksUseCase(game.playlistId)
            val loadSavedGame = getSavedGameUseCase()

            if (loadSavedGame != null) {
                _state.update {
                    it.copy(
                        game = loadSavedGame,
                        tracks = MockMusicData.songs,
                    )}
                val nextTrack = drawNextTrack(_state.value)
                _state.update { it.copy(currentTrack = nextTrack) }
            } else {
                startNewGame()
            }
        }
    }
    fun startNewGame() {
        viewModelScope.launch {
            resetGameUseCase()
            _state.update {
                it.copy(
                    game = MockGameData.game,
                    tracks = MockMusicData.songs
                )
            }
            initGame()
        }
    }

    private fun initGame() {
        // TODO: Replace with initGameUseCase()
        // Should assign one unique starter track per team from the playlist
        // and set the first currentTrack to the next available track
        val currentState = _state.value
        val game = currentState.game ?: return
        val tracks = currentState.tracks.shuffled()

        val updatedMap = game.collectedCardsByTeam.toMutableMap()
        game.teams.forEachIndexed { index, team ->
            val starterTrack = tracks.getOrNull(index) ?: return
            updatedMap[team] = listOf(starterTrack)
        }

        val usedTracks = updatedMap.values.flatten()
        val firstCurrentTrack = tracks.firstOrNull { it !in usedTracks } ?: return

        _state.update { oldState ->
            oldState.copy(
                game = game.copy(collectedCardsByTeam = updatedMap),
                currentTrack = firstCurrentTrack
            )
        }

        println("GameViewModel: Init - each team got a starter track")
        println("GameViewModel: Current track to guess: ${firstCurrentTrack.mainArtist} - ${firstCurrentTrack.title} (${firstCurrentTrack.releaseYear})")
    }

    private fun validateGuess(position: Int) {
        val currentState = _state.value
        val currentTrack = currentState.currentTrack ?: return
        val game = currentState.game ?: return
        val timeline = currentState.timeline

        // TODO: Replace with validateGuessUseCase(timeline, currentTrack, position)
        val isCorrect = isPositionCorrect(timeline, currentTrack, position)

        println("GameViewModel: Guessing position: $position for track: ${currentTrack.mainArtist} - ${currentTrack.title} (${currentTrack.releaseYear})")

        if (isCorrect) {
            val updatedTimeline = timeline.toMutableList().apply {
                add(position, currentTrack)
            }
            _state.update { oldState ->
                oldState.copy(
                    // TODO: Replace with addTrackToTimelineUseCase(game.currentTeam, updatedTimeline)
                    game = game.copy(
                        collectedCardsByTeam = game.collectedCardsByTeam.toMutableMap().apply {
                            put(game.currentTeam, updatedTimeline)
                        }
                    ),
                    // TODO: Replace with getNextTrackUseCase(oldState)
                    currentTrack = drawNextTrack(oldState),
                    isGuessCorrect = true
                )
            }
            println("GameViewModel: Correct! Track added at position $position | ${currentTrack.mainArtist} - ${currentTrack.title} (${currentTrack.releaseYear})")
        } else {
            _state.update { oldState ->
                oldState.copy(
                    // TODO: Replace with getNextTrackUseCase(oldState)
                    currentTrack = drawNextTrack(oldState),
                    isGuessCorrect = false
                )
            }
            println("GameViewModel: Wrong! Discarded: ${currentTrack.mainArtist} - ${currentTrack.title} (${currentTrack.releaseYear})")
        }

        if (_state.value.isGameWon) {
            // TODO: Navigate to SummaryScreen via navigation event
            _state.update { it.copy(currentTrack = null) }
            println("GameViewModel: Game over! A team reached ${GameConstants.CARDS_TO_WIN} cards")
            return
        }

        // TODO: Replace with nextTeamUseCase()
        nextTeam()
        println("GameViewModel: Next team: ${_state.value.game?.currentTeam?.name} | Next track: ${_state.value.currentTrack?.mainArtist} - ${_state.value.currentTrack?.title} (${_state.value.currentTrack?.releaseYear})")

        viewModelScope.launch {
        _state.value.game?.let { it -> saveGameProgressUseCase(it)
        println("Game state is saved with saveGameProgressUseCase")
        }
        }
    }

    private fun isPositionCorrect(timeline: List<Track>, track: Track, position: Int): Boolean {
        // TODO: Replace with validateGuessUseCase(timeline, track, position)
        // Domain rule: track must fit chronologically between neighbours
        if (position < 0 || position > timeline.size) return false

        val before = if (position > 0) timeline.getOrNull(position - 1) else null
        val after = if (position < timeline.size) timeline.getOrNull(position) else null

        return (before == null || before.releaseYear <= track.releaseYear) &&
                (after == null || after.releaseYear >= track.releaseYear)
    }

    private fun drawNextTrack(state: GameState): Track? {
        // TODO: Replace with getNextTrackUseCase(usedTracks, availableTracks)
        // Should return a random unplayed track not already in any team's timeline
        val usedTracks = state.game?.collectedCardsByTeam?.values?.flatten() ?: emptyList()
        val availableTracks = state.tracks.filter { it !in usedTracks && it != state.currentTrack }
        return availableTracks.randomOrNull()
    }

    private fun nextTeam() {
        // TODO: Replace with nextTeamUseCase()
        // Should advance currentTeam to the next team in the list, wrapping around
        val currentState = _state.value
        val game = currentState.game ?: return
        val currentIndex = game.teams.indexOfFirst { it == game.currentTeam }
        val nextTeam = game.teams[(currentIndex + 1) % game.teams.size]

        _state.update { oldState ->
            oldState.copy(
                game = oldState.game?.copy(currentTeam = nextTeam)
            )
        }
    }

    fun playMusic(trackId: String) {
        viewModelScope.launch {
            try {
                playMusicUseCase(trackId)
            } catch (e: Exception) {
                println("Error while playing: ${e.message}")
            }
        }
    }
}