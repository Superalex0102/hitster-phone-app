package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository

class RestartGameUseCase(
    private val activeGameRepository: ActiveGameRepository,
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke() {
        activeGameRepository.clearGame()
        teamRepository.clearAllTeam()
    }
}