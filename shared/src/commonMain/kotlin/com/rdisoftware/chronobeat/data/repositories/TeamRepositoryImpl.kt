package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.presentation.constants.GameConstants.SAVED_TEAMS
import com.russhwolf.settings.Settings
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TeamRepositoryImpl(
    private val settings: Settings
) : TeamRepository {
    private val _teams = mutableMapOf<Uuid, Team>()
    private val mutex = Mutex()

    init {
        val savedTeamJson = settings.getStringOrNull(SAVED_TEAMS)
        if (savedTeamJson != null) {
            try {
                val teamList = Json.decodeFromString<List<Team>>(savedTeamJson)
                teamList.forEach { _teams[it.id] = it }
            } catch (_: Exception) {
            }
        }
    }

    private fun persistTeams() {
        val jsonString = Json.encodeToString(_teams.values.toList())
        settings.putString(SAVED_TEAMS, jsonString)
    }

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
            persistTeams()
            newTeam
        }
    }

    override suspend fun updateTeamName(
        team: Team
    ): Team {
        return mutex.withLock {
            if (!_teams.containsKey(team.id)) {
                throw IllegalArgumentException("Team with id ${team.id} not found")
            }
            _teams[team.id] = team
            persistTeams()
            team
        }
    }

    override suspend fun deleteTeam(teamId: Uuid) {
        mutex.withLock {
            _teams.remove(teamId) ?: throw IllegalArgumentException("Team with id $teamId not found")
            persistTeams()
        }
    }

    override suspend fun clearAllTeam() {
        mutex.withLock {
            _teams.clear()
            settings.remove(SAVED_TEAMS)
        }
    }
}