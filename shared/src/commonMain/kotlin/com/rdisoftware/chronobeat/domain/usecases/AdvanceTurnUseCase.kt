package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import kotlin.uuid.ExperimentalUuidApi

class AdvanceTurnUseCase {
    @OptIn(ExperimentalUuidApi::class)
    operator fun invoke(currentDto: GameDto): String {
        val currentIndex = currentDto.teamIds.indexOf(currentDto.currentTeamId)
        val nextTeamId = currentDto.teamIds[(currentIndex + 1) % currentDto.teamIds.size]
        return nextTeamId.toString()
    }
}