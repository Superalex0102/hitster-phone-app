package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository

class SaveGameProgressUseCase(
    private val activeGameRepository: ActiveGameRepository
) {
    suspend operator fun invoke(game: Game) {
        activeGameRepository.saveGame(game)
    }
}