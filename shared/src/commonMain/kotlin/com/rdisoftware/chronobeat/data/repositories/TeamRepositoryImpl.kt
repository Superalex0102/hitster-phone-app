package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TeamRepositoryImpl : TeamRepository {
    private val _teams = mutableMapOf<Uuid, Team>()
    private val mutex = Mutex()

    override suspend fun getTeams(): List<Team> {
        return mutex.withLock {
            _teams.values.toList()
        }
    }

    override suspend fun createTeam(
        teamName: String,
        color: TeamColor
    ): Team {
        val newUuid = Uuid.random()
        val newTeam = Team(newUuid, teamName, color)

        return mutex.withLock {
            _teams[newUuid] = newTeam
            newTeam
        }
    }

    override suspend fun editTeamName(
        team: Team
    ): Team {
        return mutex.withLock {
            if (!_teams.containsKey(team.id)) {
                throw IllegalArgumentException("Team with id ${team.id} not found")
            }

            _teams[team.id] = team
            team
        }
    }

    override suspend fun deleteTeam(teamId: Uuid) {
        mutex.withLock {
            _teams.remove(teamId) ?: throw IllegalArgumentException("Team with id $teamId not found")
        }
    }
}