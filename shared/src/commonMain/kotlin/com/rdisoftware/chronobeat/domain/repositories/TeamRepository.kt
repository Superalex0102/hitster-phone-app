package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
interface TeamRepository {
    suspend fun getTeams(): List<Team>
    suspend fun createTeam(teamName: String, color: TeamColor): Team
    suspend fun editTeamName(teamId: Uuid, newTeamName: String): Team
    suspend fun deleteTeam(teamId: Uuid)
}