package com.rdisoftware.chronobeat.domain.usecases.team

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository

class AddTeamUseCase(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(teamName: String, color: TeamColor) {
        teamRepository.createTeam(teamName, color)
    }
}