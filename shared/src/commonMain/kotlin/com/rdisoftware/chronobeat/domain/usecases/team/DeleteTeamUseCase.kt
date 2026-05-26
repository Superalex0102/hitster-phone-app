package com.rdisoftware.chronobeat.domain.usecases.team

import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteTeamUseCase (
    private val teamRepository: TeamRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(teamId: Uuid) {
        teamRepository.deleteTeam(teamId)
    }
}