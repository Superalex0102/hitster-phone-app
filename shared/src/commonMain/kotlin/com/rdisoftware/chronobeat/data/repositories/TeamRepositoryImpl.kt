package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TeamRepositoryImpl : TeamRepository {
    private val _teams = mutableMapOf<Uuid, Team>()

    override suspend fun getTeams(): List<Team> {
        return _teams.values.toList()
    }

    override suspend fun createTeam(
        teamName: String,
        color: TeamColor
    ): Team {
        val newUuid = Uuid.random()
        val newTeam = Team(newUuid, teamName, color)
        _teams[newUuid] = newTeam
        return newTeam
    }

    override suspend fun deleteTeam(teamId: Uuid) {
        _teams.remove(teamId)
    }
}