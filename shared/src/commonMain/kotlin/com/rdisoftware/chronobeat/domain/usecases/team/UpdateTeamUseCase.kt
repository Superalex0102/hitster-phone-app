package com.rdisoftware.chronobeat.domain.usecases.team

import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository

class UpdateTeamUseCase (
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(team: Team) {
        teamRepository.editTeamName(team)
    }
}