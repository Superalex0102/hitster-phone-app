package com.rdisoftware.chronobeat.domain.usecases.game

import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository

class ClearGameUseCase(
    private val activeGameRepository: ActiveGameRepository,
) {
    suspend operator fun invoke() {
        activeGameRepository.clearGame()
    }
}