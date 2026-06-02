package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SetupInitialGameUseCase(
    private val getPlayableTrackUseCase: GetPlayableTrackUseCase,
    private val activeGameRepository: ActiveGameRepository
) {
    suspend operator fun invoke(playlistId: String, teams: List<Team>, trackIdPool: MutableList<String>): Game? {
        val collectedCardIds = mutableMapOf<Uuid, List<String>>()
        val collectedCards = mutableMapOf<Team, List<Track>>()

        teams.forEach { team ->
            val starterTrack = getPlayableTrackUseCase(trackIdPool) ?: return null

            collectedCardIds[team.id] = listOf(starterTrack.id)
            collectedCards[team] = listOf(starterTrack)
        }

        val firstCurrentTrack = getPlayableTrackUseCase(trackIdPool) ?: return null

        val gameId = Uuid.random()

        val initialDto = GameDto(
            id = gameId,
            teamIds = teams.map { it.id },
            currentTeamId = teams.first().id,
            currentTrackId = firstCurrentTrack.id,
            collectedCardIdsByTeamId = collectedCardIds,
            playlistId = playlistId,
            winnerTeamId = null
        )
        activeGameRepository.saveGame(initialDto)

        return Game(
            id = gameId,
            teams = teams,
            currentTeam = teams.first(),
            currentTrack = firstCurrentTrack,
            collectedCardsByTeam = collectedCards,
            playlistId = playlistId,
            winnerTeam = null
        )
    }
}