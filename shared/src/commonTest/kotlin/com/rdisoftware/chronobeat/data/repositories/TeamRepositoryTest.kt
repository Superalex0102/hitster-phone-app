package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TeamRepositoryTest {

    private lateinit var repository: TeamRepository

    @BeforeTest
    fun setup() {
        repository = TeamRepositoryImpl()
    }

    @Test
    fun `getTeams should return empty list initially`() = runTest {
        val teams = repository.getTeams()
        assertTrue(teams.isEmpty(), "Initial team list should be empty")
    }

    @Test
    fun `createTeam should add a new team and return it`() = runTest {
        val teamName = "Piros Alma"
        val color = TeamColor.CRIMSON

        val createdTeam = repository.createTeam(teamName, color)
        val allTeams = repository.getTeams()

        assertEquals(teamName, createdTeam.name)
        assertEquals(color, createdTeam.color)
        assertEquals(1, allTeams.size)
        assertEquals(createdTeam, allTeams.first())
    }

    @Test
    fun `updateTeamName should update the name of an existing team`() = runTest {
        val originalTeam = repository.createTeam("Old Name", TeamColor.PLUM)
        val newName = "New Awesome Name"

        val teamToUpdate = originalTeam.copy(name = newName)
        val updatedTeam = repository.updateTeamName(teamToUpdate)
        val fetchedTeam = repository.getTeams().first { it.id == originalTeam.id }

        assertEquals(newName, updatedTeam.name)
        assertEquals(newName, fetchedTeam.name)
        assertEquals(TeamColor.PLUM, updatedTeam.color, "Color should remain unchanged")
    }

    @Test
    fun `updateTeamName should throw IllegalArgumentException for non-existent id`() = runTest {
        val fakeId = Uuid.random()

        val fakeTeam = Team(
            id = fakeId,
            name = "Hacker Team",
            color = TeamColor.PLUM
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            repository.updateTeamName(fakeTeam)
        }

        assertTrue(
            actual = exception.message!!.contains("not found"),
            message = "The exception message should indicate that the team was not found"
        )
    }

    @Test
    fun `deleteTeam should remove the team from the list`() = runTest {
        val team1 = repository.createTeam("Team 1", TeamColor.PLUM)
        val team2 = repository.createTeam("Team 2", TeamColor.AMBER)

        repository.deleteTeam(team1.id)
        val remainingTeams = repository.getTeams()

        assertEquals(1, remainingTeams.size)
        assertEquals(team2.id, remainingTeams.first().id)
    }

    @Test
    fun `deleteTeam should throw IllegalArgumentException for non-existent id`() = runTest {
        val fakeId = Uuid.random()

        assertFailsWith<IllegalArgumentException> {
            repository.deleteTeam(fakeId)
        }
    }
}