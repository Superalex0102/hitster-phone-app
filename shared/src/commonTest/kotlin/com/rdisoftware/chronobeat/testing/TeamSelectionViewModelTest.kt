package com.rdisoftware.chronobeat.testing

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.game.ClearGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalUuidApi::class
)
class TeamSelectionViewModelTest {
    lateinit var viewModel: TeamSelectionViewModel
    lateinit var fakeActiveGameRepository: FakeActiveGameRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        fakeActiveGameRepository = FakeActiveGameRepository()

        val fakeRepository = object : TeamRepository {
            val list = mutableListOf<Team>()

            override suspend fun getTeams(): List<Team> = list
            override suspend fun createTeam(teamName: String, color: TeamColor): Team {
                val team = Team(Uuid.random(), teamName, color)
                list.add(team)
                return team
            }

            override suspend fun deleteTeam(teamId: Uuid) {
                list.removeAll { it.id == teamId }
            }

            override suspend fun clearAllTeam() {
                TODO("Not yet implemented")
            }

            override suspend fun updateTeamName(team: Team): Team {
                val index = list.indexOfFirst { it.id == team.id }
                if (index != -1) list[index] = team
                return team
            }
        }

        val getTeamsUseCase = GetTeamsUseCase(fakeRepository)
        val addTeamUseCase = AddTeamUseCase(fakeRepository)
        val deleteTeamUseCase = DeleteTeamUseCase(fakeRepository)
        val updateTeamUseCase = UpdateTeamUseCase(fakeRepository)
        val clearGameUseCase = ClearGameUseCase(fakeActiveGameRepository)

        viewModel = TeamSelectionViewModel(
            addTeamUseCase = addTeamUseCase,
            deleteTeamUseCase = deleteTeamUseCase,
            updateTeamUseCase = updateTeamUseCase,
            getTeamsUseCase = getTeamsUseCase,
            clearGameUseCase = clearGameUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.addTeam(name: String) {
        viewModel.onNameChanged(newName = name)
        viewModel.addTeam()
        advanceUntilIdle()
    }

    @Test
    fun `addTeam should add a new team when input is valid`() = runTest {
        addTeam("Team 1")

        assertEquals(
            expected = 1,
            actual = viewModel.state.value.teams.size,
            message = "addTeam should add a new team to the list when input is valid"
        )
        assertEquals(
            expected = "Team 1",
            actual = viewModel.state.value.teams.first().name,
            message = "addTeam should use the provided input name"
        )
    }

    @Test
    fun `addTeam should not add team when max limit is reached`() = runTest {
        addTeam("Team 1")
        addTeam("Team 2")
        addTeam("Team 3")
        addTeam("Team 4")
        addTeam("Team 5")

        assertEquals(
            expected = 4,
            actual = viewModel.state.value.teams.size,
            message = "addTeam should not add more teams when max limit is reached"
        )
    }

    @Test
    fun `addTeam should not add team when input contains only whitespace`() = runTest {
        addTeam("   ")

        assertTrue(
            actual = viewModel.state.value.teams.isEmpty(),
            message = "addTeam doesn't take white space strings as an input"
        )
    }

    @Test
    fun `addTeam should trim out the white spaces`() = runTest {
        addTeam("Team 1")
        addTeam("   Team 2   ")
        val secondTeam = viewModel.state.value.teams[1].name


        assertEquals(
            expected = "Team 2",
            actual = secondTeam,
            message = "addTeam should trim input name before adding team"
        )
    }

    @Test
    fun `deleteTeam should delete existing team`() = runTest {
        addTeam("Team 1")
        advanceUntilIdle()
        viewModel.deleteTeam(teamId = viewModel.state.value.teams.first().id)

        assertTrue(
            actual = viewModel.state.value.teams.isEmpty(),
            message = "deleteTeam should remove team from list when ID exists"
        )
    }

    @Test
    fun `onNameChanged should ignore values when exceeding max length`() {
        viewModel.onNameChanged("Valid")
        viewModel.onNameChanged("Longer than 16 characters")

        assertEquals(
            expected = "Valid",
            actual = viewModel.state.value.inputName,
            message = "onNameChanged should ignore input exceeding max length"
        )
    }

    @Test
    fun `startEdit should set editing state correctly`() = runTest {
        addTeam("Team 1")
        advanceUntilIdle()
        val team = viewModel.state.value.teams.first()
        viewModel.startEdit(team)
        val state = viewModel.state.value

        assertEquals(
            expected = team,
            actual = state.editingTeam,
            message = "startEdit should set the editingTeam"
        )

        assertEquals(
            expected = team.name,
            actual = state.inputName,
            message = "startEdit should copy team name to inputName"
        )

        assertTrue(
            actual = state.isEditing,
            message = "startEdit should set isEditing to true"
        )
    }

    @Test
    fun `confirmEdit should update team name when input is valid`() = runTest {
        addTeam("Team 1")
        advanceUntilIdle()
        val team = viewModel.state.value.teams.first()
        viewModel.startEdit(team)
        viewModel.onNameChanged("Team 5")
        viewModel.confirmEdit()

        assertEquals(
            expected = "Team 5",
            actual = viewModel.state.value.teams.first().name,
            message = "confirmEdit should update team name when input is within max length"
        )
    }

    @Test
    fun `confirmEdit should not update team when input is invalid`() = runTest {
        addTeam("Team 1")
        advanceUntilIdle()
        val team = viewModel.state.value.teams.first()
        viewModel.startEdit(team)
        viewModel.onNameChanged("Longer than 16 characters")
        viewModel.confirmEdit()

        assertEquals(
            expected = team.name,
            actual = viewModel.state.value.teams.first().name,
            message = "confirmEdit should not update team name when input exceeds max length"
        )
    }

    @Test
    fun `updateTeam should replace team when ID matches`() = runTest {
        addTeam("Team 1")
        val originalTeam = viewModel.state.value.teams.first()

        val updatedTeam = originalTeam.copy(name = "Updated team")
        viewModel.updateTeam(updatedTeam)

        assertEquals(
            expected = "Updated team",
            actual = viewModel.state.value.teams.first().name,
            message = "updateTeam should replace the team's name with the updated value"
        )
    }

    @Test
    fun `onColorChanged updates selectedColor`() {
        val newColor = TeamColor.entries[3]

        viewModel.onColorChanged(newColor)

        assertEquals(
            expected = newColor,
            actual = viewModel.state.value.selectedColor,
            message = "onColorChanged should update selectedColor"
        )
    }

    @Test
    fun `clearGame should call repository clearGame`() = runTest {
        viewModel.clearGame()
        advanceUntilIdle()

        assertTrue(
            actual = fakeActiveGameRepository.cleared,
            message = "clearGame should call repository clearGame()"
        )
    }
}

class FakeActiveGameRepository : ActiveGameRepository {

    var cleared = false

    override suspend fun getGame(): GameDto? = null

    override suspend fun saveGame(game: GameDto) {}

    override suspend fun clearGame() {
        cleared = true
    }

    override fun observeGame(): Flow<GameDto?> {
        return MutableStateFlow(null)
    }
}