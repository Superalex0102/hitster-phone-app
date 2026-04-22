package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TeamRepository {
    suspend fun getTeams(): List<Team>
    suspend fun createTeam(teamName: String, color: TeamColor): Team
    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteTeam(teamId: Uuid)
}