package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import kotlin.uuid.ExperimentalUuidApi

class LoadGameSessionUseCase(private val musicRepository: MusicRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(dto: GameDto): Map<String, Track> {
        val trackMap = mutableMapOf<String, Track>()
        val allNeededIds = dto.collectedCardIdsByTeamId.values.flatten() + dto.currentTrackId

        allNeededIds.distinct().forEach { id ->
            runCatching {
                val track = musicRepository.getTrackInfo(id)
                trackMap[id] = track
            }
        }
        return trackMap
    }
}