package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import kotlin.collections.removeAll
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class FakeTeamRepository(var teams: MutableList<Team> = mutableListOf()) : TeamRepository {

    override suspend fun getTeams(): List<Team> = teams

    override suspend fun createTeam(teamName: String, color: TeamColor): Team {
        val newTeam = Team(
            id = Uuid.random(),
            name = teamName,
            color = color
        )
        teams.add(newTeam)
        return newTeam
    }

    override suspend fun updateTeamName(team: Team): Team {
        val index = teams.indexOfFirst { it.id == team.id }
        if (index != -1) {
            teams[index] = team
            return team
        }
        return team
    }

    override suspend fun deleteTeam(teamId: Uuid) {
        teams.removeAll { it.id == teamId }
    }

    override suspend fun clearAllTeam() {
        teams.clear()
    }
}