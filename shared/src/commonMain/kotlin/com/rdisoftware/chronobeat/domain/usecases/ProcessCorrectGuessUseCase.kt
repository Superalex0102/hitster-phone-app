package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProcessCorrectGuessUseCase {
    operator fun invoke(currentDto: GameDto, trackId: String, position: Int): Pair<Map<Uuid, List<String>>, Uuid?> {
        val currentTeamId = currentDto.currentTeamId
        val currentTrackIds = currentDto.collectedCardIdsByTeamId[currentTeamId] ?: emptyList()

        val updatedTrackIds = currentTrackIds.toMutableList().apply {
            add(position, trackId)
        }

        val newCollectedCards = currentDto.collectedCardIdsByTeamId.toMutableMap().apply {
            put(currentTeamId, updatedTrackIds)
        }

        val winnerId = if (updatedTrackIds.size >= GameConstants.CARDS_TO_WIN) currentTeamId else null

        return Pair(newCollectedCards, winnerId)
    }
}