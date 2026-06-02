package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository

class GetGameUseCase(private val activeGameRepository: ActiveGameRepository) {
    suspend operator fun invoke(): GameDto? {
        return activeGameRepository.getGame()
    }
}
