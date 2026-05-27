package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class TeamRepositoryIntegrationTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: TeamSelectionViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val repository = TeamRepositoryImpl()
        viewModel = TeamSelectionViewModel(
            addTeamUseCase    = AddTeamUseCase(repository),
            deleteTeamUseCase = DeleteTeamUseCase(repository),
            updateTeamUseCase = UpdateTeamUseCase(repository),
            getTeamsUseCase   = GetTeamsUseCase(repository)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------------------
    // Initial state
    // -------------------------------------------------------------------------

    @Test
    fun `initial state has no teams and canStartGame is false`() =
        runTest(testDispatcher) {
            advanceUntilIdle()
            val state = viewModel.state.value

            assertTrue(state.teams.isEmpty())
            assertFalse(state.canStartGame)
        }

    @Test
    fun `initial state canAddTeam is false because inputName is blank`() =
        runTest(testDispatcher) {
            advanceUntilIdle()

            assertFalse(viewModel.state.value.canAddTeam)
        }

    // -------------------------------------------------------------------------
    // addTeam flow
    // -------------------------------------------------------------------------

    @Test
    fun `typing a name makes canAddTeam true`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Red Team")
            advanceUntilIdle()

            assertTrue(viewModel.state.value.canAddTeam)
        }

    @Test
    fun `addTeam stores the team and clears the input`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Red Team")
            viewModel.addTeam()
            advanceUntilIdle()

            val state = viewModel.state.value
            assertEquals(1, state.teams.size)
            assertEquals("Red Team", state.teams.first().name)
            assertEquals("", state.inputName)
        }

    @Test
    fun `addTeam trims leading and trailing whitespace`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("  Blue Team  ")
            viewModel.addTeam()
            advanceUntilIdle()

            assertEquals("Blue Team", viewModel.state.value.teams.first().name)
        }

    @Test
    fun `addTeam persists the selected color`() =
        runTest(testDispatcher) {
            viewModel.onColorChanged(TeamColor.AMBER)
            viewModel.onNameChanged("Yellow")
            viewModel.addTeam()
            advanceUntilIdle()

            assertEquals(TeamColor.AMBER, viewModel.state.value.teams.first().color)
        }

    @Test
    fun `addTeam does nothing when canAddTeam is false (blank name)`() =
        runTest(testDispatcher) {
            viewModel.addTeam()
            advanceUntilIdle()

            assertTrue(viewModel.state.value.teams.isEmpty())
        }

    @Test
    fun `selectedColor auto-advances to an unused color after addTeam`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("First")
            viewModel.addTeam()
            advanceUntilIdle()

            val secondColor = viewModel.state.value.selectedColor
            assertFalse(
                viewModel.state.value.teams.any { it.color == secondColor },
                "Auto-selected color must not already be used"
            )
        }

    @Test
    fun `canStartGame becomes true after adding the minimum required teams`() =
        runTest(testDispatcher) {
            repeat(2) { i ->
                viewModel.onNameChanged("Team $i")
                viewModel.addTeam()
                advanceUntilIdle()
            }

            assertTrue(viewModel.state.value.canStartGame)
        }

    @Test
    fun `isMaxReached becomes true and canAddTeam false after reaching MAX_TEAMS`() =
        runTest(testDispatcher) {
            repeat(6) { i ->
                viewModel.onNameChanged("T$i")
                viewModel.addTeam()
                advanceUntilIdle()
            }

            val state = viewModel.state.value
            assertTrue(state.isMaxReached)
            assertFalse(state.canAddTeam, "canAddTeam must be false when max teams reached")
        }

    // -------------------------------------------------------------------------
    // deleteTeam flow
    // -------------------------------------------------------------------------

    @Test
    fun `deleteTeam removes the correct team from state`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("To Delete")
            viewModel.addTeam()
            advanceUntilIdle()
            viewModel.onNameChanged("To Keep")
            viewModel.addTeam()
            advanceUntilIdle()

            val toDelete = viewModel.state.value.teams.first { it.name == "To Delete" }
            viewModel.deleteTeam(toDelete.id)
            advanceUntilIdle()

            val remaining = viewModel.state.value.teams
            assertEquals(1, remaining.size)
            assertEquals("To Keep", remaining.first().name)
        }

    @Test
    fun `deleting the last team makes canStartGame false again`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Only")
            viewModel.addTeam()
            advanceUntilIdle()

            val team = viewModel.state.value.teams.first()
            viewModel.deleteTeam(team.id)
            advanceUntilIdle()

            assertFalse(viewModel.state.value.canStartGame)
        }

    // -------------------------------------------------------------------------
    // edit flow
    // -------------------------------------------------------------------------

    @Test
    fun `startEdit sets isEditing true and populates inputName`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Original")
            viewModel.addTeam()
            advanceUntilIdle()

            val team = viewModel.state.value.teams.first()
            viewModel.startEdit(team)

            val state = viewModel.state.value
            assertTrue(state.isEditing)
            assertEquals("Original", state.inputName)
            assertEquals(team, state.editingTeam)
        }

    @Test
    fun `confirmEdit updates the team name end-to-end`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Old Name")
            viewModel.addTeam()
            advanceUntilIdle()

            val team = viewModel.state.value.teams.first()
            viewModel.startEdit(team)
            viewModel.onNameChanged("New Name")
            viewModel.confirmEdit()
            advanceUntilIdle()

            val updated = viewModel.state.value.teams.first { it.id == team.id }
            assertEquals("New Name", updated.name)
        }

    @Test
    fun `confirmEdit clears editing state and inputName`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Edit Me")
            viewModel.addTeam()
            advanceUntilIdle()

            val team = viewModel.state.value.teams.first()
            viewModel.startEdit(team)
            viewModel.onNameChanged("Done")
            viewModel.confirmEdit()
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isEditing)
            assertNull(state.editingTeam)
            assertEquals("", state.inputName)
        }

    @Test
    fun `confirmEdit does nothing when no team is being edited`() =
        runTest(testDispatcher) {
            viewModel.onNameChanged("Orphan")
            viewModel.confirmEdit()
            advanceUntilIdle()

            assertTrue(viewModel.state.value.teams.isEmpty())
        }

    @Test
    fun `confirmEdit preserves the team color`() =
        runTest(testDispatcher) {
            viewModel.onColorChanged(TeamColor.PLUM)
            viewModel.onNameChanged("Purple Team")
            viewModel.addTeam()
            advanceUntilIdle()

            val team = viewModel.state.value.teams.first()
            viewModel.startEdit(team)
            viewModel.onNameChanged("Purple Team Renamed")
            viewModel.confirmEdit()
            advanceUntilIdle()

            assertEquals(TeamColor.PLUM, viewModel.state.value.teams.first().color)
        }

    // -------------------------------------------------------------------------
    // onNameChanged validation
    // -------------------------------------------------------------------------

    @Test
    fun `onNameChanged ignores input exceeding MAX_NAME_LENGTH`() =
        runTest(testDispatcher) {
            val tooLong = "A".repeat(200)
            viewModel.onNameChanged(tooLong)

            assertTrue(
                viewModel.state.value.inputName.length < tooLong.length,
                "Name longer than MAX_NAME_LENGTH must be rejected"
            )
        }

    // -------------------------------------------------------------------------
    // Full lifecycle scenario
    // -------------------------------------------------------------------------

    @Test
    fun `full lifecycle — add two teams, rename one, delete the other`() =
        runTest(testDispatcher) {
            viewModel.onColorChanged(TeamColor.CRIMSON)
            viewModel.onNameChanged("Red Team")
            viewModel.addTeam()
            advanceUntilIdle()

            viewModel.onColorChanged(TeamColor.AMBER)
            viewModel.onNameChanged("Yellow Team")
            viewModel.addTeam()
            advanceUntilIdle()

            assertEquals(2, viewModel.state.value.teams.size)
            assertTrue(viewModel.state.value.canStartGame)

            val red = viewModel.state.value.teams.first { it.color == TeamColor.CRIMSON }
            viewModel.startEdit(red)
            viewModel.onNameChanged("Scarlet Team")
            viewModel.confirmEdit()
            advanceUntilIdle()

            val renamed = viewModel.state.value.teams.first { it.id == red.id }
            assertEquals("Scarlet Team", renamed.name)

            val yellow = viewModel.state.value.teams.first { it.color == TeamColor.AMBER }
            viewModel.deleteTeam(yellow.id)
            advanceUntilIdle()

            val finalState = viewModel.state.value
            assertEquals(1, finalState.teams.size)
            assertEquals("Scarlet Team", finalState.teams.first().name)
            assertFalse(finalState.canStartGame)
        }
}