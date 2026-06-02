package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SetupInitialGameUseCase(
    private val getPlayableTrackUseCase: GetPlayableTrackUseCase,
    private val activeGameRepository: ActiveGameRepository
) {
    suspend operator fun invoke(playlistId: String, teams: List<Team>, trackIdPool: MutableList<String>): GameDto? {
        val updatedMap = mutableMapOf<String, List<String>>()

        teams.forEach { team ->
            val starterTrack = getPlayableTrackUseCase(trackIdPool) ?: return null
            updatedMap[team.id.toString()] = listOf(starterTrack.id)
        }

        val firstCurrentTrack = getPlayableTrackUseCase(trackIdPool) ?: return null

        val initialDto = GameDto(
            id = Uuid.random(),
            teamIds = teams.map { it.id },
            currentTeamId = teams.first().id,
            currentTrackId = firstCurrentTrack.id,
            collectedCardIdsByTeamId = updatedMap.mapKeys { Uuid.parse(it.key) },
            playlistId = playlistId,
            winnerTeamId = null
        )

        activeGameRepository.saveGame(initialDto)
        return initialDto
    }
}