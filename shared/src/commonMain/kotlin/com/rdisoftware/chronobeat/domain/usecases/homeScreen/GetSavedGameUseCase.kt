package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository

class GetSavedGameUseCase(
    private val activeGameRepository: ActiveGameRepository
) {
    suspend operator fun invoke(): GameDto? = activeGameRepository.getGame()
}