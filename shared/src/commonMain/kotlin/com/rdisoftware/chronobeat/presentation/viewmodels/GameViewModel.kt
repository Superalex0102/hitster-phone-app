package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import com.rdisoftware.chronobeat.presentation.preview.MockGameData
import com.rdisoftware.chronobeat.presentation.preview.MockMusicData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class GameState(
    val game: Game? = null,
    val tracks: List<Track> = emptyList(),
    val currentTrack: Track? = null,
    val isGuessCorrect: Boolean? = null,
) {
    val currentTeam = game?.currentTeam
    val timeline: List<Track> = game?.collectedCardsByTeam?.get(currentTeam) ?: emptyList()
    val currentCardCount: Int = timeline.size
    val isGameWon: Boolean = game?.winnerTeam != null
}

@OptIn(ExperimentalUuidApi::class)
class GameViewModel(
    private val activeGameRepository: ActiveGameRepository,
    private val playMusicUseCase: PlayMusicUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val allTeams = MockGameData.teams
    private val allTracks = MockMusicData.songs

    init {
        _state.update { it.copy(tracks = allTracks) }
        viewModelScope.launch {
            activeGameRepository.observeGame().collect { dto ->
                if (dto != null) {
                    val mappedGame = mapDtoToGame(dto)
                    _state.update {
                        it.copy(
                            game = mappedGame,
                            currentTrack = mappedGame.currentTrack
                        )
                    }
                    println("GameViewModel: Current team: ${mappedGame.currentTeam.name} | Track to guess: ${mappedGame.currentTrack.mainArtist} - ${mappedGame.currentTrack.title} (${mappedGame.currentTrack.releaseYear})")
                }
            }
        }

        initGame()
    }

    private fun mapDtoToGame(dto: GameDto): Game {
        val teams = dto.teamIds.mapNotNull { id -> allTeams.find { it.id == id } }
        val currentTeam = allTeams.first { it.id == dto.currentTeamId }
        val currentTrack = allTracks.first { it.id == dto.currentTrackId }
        val winnerTeam = dto.winnerTeamId?.let { id -> allTeams.find { it.id == id } }

        val collectedCards = dto.collectedCardIdsByTeamId.mapNotNull { (teamId, trackIds) ->
            val team = allTeams.find { it.id == teamId } ?: return@mapNotNull null
            val tracks = trackIds.mapNotNull { trackId -> allTracks.find { it.id == trackId } }
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

    private fun initGame() {
        viewModelScope.launch {
            val tracks = allTracks.shuffled()
            val updatedMap = mutableMapOf<Uuid, List<String>>()

            allTeams.forEachIndexed { index, team ->
                val starterTrack = tracks.getOrNull(index) ?: return@launch
                updatedMap[team.id] = listOf(starterTrack.id)
            }

            val usedTrackIds = updatedMap.values.flatten()
            val firstCurrentTrack = tracks.firstOrNull { it.id !in usedTrackIds } ?: return@launch

            val initialDto = GameDto(
                id = Uuid.random(),
                teamIds = allTeams.map { it.id },
                currentTeamId = allTeams.first().id,
                currentTrackId = firstCurrentTrack.id,
                collectedCardIdsByTeamId = updatedMap,
                playlistId = "mock_playlist",
                winnerTeamId = null
            )

            activeGameRepository.saveGame(initialDto)
            println("GameViewModel: Init - each team got a starter track")
        }
    }

    fun onGuessPressed(position: Int) {
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

        println("GameViewModel: Guessing position: $position for track: ${currentTrack.mainArtist} - ${currentTrack.title} (${currentTrack.releaseYear})")

        var nextTrackId = currentDto.currentTrackId
        var winnerId: Uuid? = null
        var newCollectedCards = currentDto.collectedCardIdsByTeamId

        if (isCorrect) {
            val currentTrackIds = currentDto.collectedCardIdsByTeamId[currentDto.currentTeamId] ?: emptyList()
            val updatedTrackIds = currentTrackIds.toMutableList().apply { add(position, currentTrack.id) }

            newCollectedCards = currentDto.collectedCardIdsByTeamId.toMutableMap().apply {
                put(currentDto.currentTeamId, updatedTrackIds)
            }

            _state.update { it.copy(isGuessCorrect = true) }
            println("GameViewModel: Correct! Track added at position $position")

            if (updatedTrackIds.size >= GameConstants.CARDS_TO_WIN) {
                winnerId = currentDto.currentTeamId
                println("GameViewModel: Game over! A team reached ${GameConstants.CARDS_TO_WIN} cards")
            }
        } else {
            _state.update { it.copy(isGuessCorrect = false) }
            println("GameViewModel: Wrong! Discarded: ${currentTrack.mainArtist}")
        }

        var nextTeamId = currentDto.currentTeamId
        if (winnerId == null) {
            nextTrackId = drawNextTrackId(newCollectedCards) ?: currentDto.currentTrackId

            val currentIndex = currentDto.teamIds.indexOf(currentDto.currentTeamId)
            nextTeamId = currentDto.teamIds[(currentIndex + 1) % currentDto.teamIds.size]
        }

        val updatedDto = currentDto.copy(
            collectedCardIdsByTeamId = newCollectedCards,
            currentTrackId = nextTrackId,
            winnerTeamId = winnerId,
            currentTeamId = nextTeamId
        )
        activeGameRepository.saveGame(updatedDto)
    }

    private fun isPositionCorrect(timeline: List<Track>, track: Track, position: Int): Boolean {
        if (position < 0 || position > timeline.size) return false

        val before = if (position > 0) timeline.getOrNull(position - 1) else null
        val after = if (position < timeline.size) timeline.getOrNull(position) else null

        return (before == null || before.releaseYear <= track.releaseYear) &&
                (after == null || after.releaseYear >= track.releaseYear)
    }

    private fun drawNextTrackId(collectedCards: Map<Uuid, List<String>>): String? {
        val usedTrackIds = collectedCards.values.flatten()
        val availableTracks = allTracks.filter { it.id !in usedTrackIds }
        return availableTracks.randomOrNull()?.id
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