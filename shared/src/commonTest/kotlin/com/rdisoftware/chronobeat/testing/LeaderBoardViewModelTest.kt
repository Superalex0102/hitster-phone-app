package com.rdisoftware.chronobeat.testing

import FakeMusicRepository
import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.TeamRepositoryImpl
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.usecases.game.GetGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SaveGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SetupInitialGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetChronobeatPlaylistsUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetPlayableTrackUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardEffect
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardEvent
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardViewModel
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class LeaderBoardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: LeaderBoardViewModel
    private lateinit var getGameUseCase: GetGameUseCase
    private lateinit var saveGameUseCase: SaveGameUseCase
    private lateinit var getTeamsUseCase: GetTeamsUseCase
    private lateinit var addTeamUseCase: AddTeamUseCase
    private lateinit var getPlaylistsUseCase: GetChronobeatPlaylistsUseCase
    private lateinit var getPlayableTrackUseCase: GetPlayableTrackUseCase
    private lateinit var setupInitialGameUseCase: SetupInitialGameUseCase

    @BeforeTest
    fun setup() {
        val settings = MapSettings()
        Dispatchers.setMain(testDispatcher)

        val teamRepository = TeamRepositoryImpl(settings)
        val activeGameRepository = ActiveGameRepositoryImpl(settings)
        val fakeMusicRepository = FakeMusicRepository()

        getTeamsUseCase = GetTeamsUseCase(teamRepository)
        addTeamUseCase = AddTeamUseCase(teamRepository)
        getGameUseCase = GetGameUseCase(
            activeGameRepository,
            fakeMusicRepository,
            getTeamsUseCase
        )
        saveGameUseCase = SaveGameUseCase(activeGameRepository)
        getPlaylistsUseCase = GetChronobeatPlaylistsUseCase(fakeMusicRepository)
        getPlayableTrackUseCase = GetPlayableTrackUseCase(fakeMusicRepository)
        setupInitialGameUseCase = SetupInitialGameUseCase(
            getPlayableTrackUseCase,
            activeGameRepository
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init - loads points by team correctly ordered in descending order`() = testScope.runTest {
        addTeamUseCase(teamName = "Team 1", color = TeamColor.PLUM)
        addTeamUseCase(teamName = "Team 2", color = TeamColor.CRIMSON)
        addTeamUseCase(teamName = "Team 3", color = TeamColor.AMBER)

        val teams = getTeamsUseCase()
        val playlist = getPlaylistsUseCase().first()
        val trackPool = playlist.trackIds.toMutableList()

        val initialGame = setupInitialGameUseCase(playlist.id, teams, trackPool)

        val updatedCards = initialGame!!.collectedCardsByTeam.toMutableMap()

        updatedCards[teams[0]] = updatedCards[teams[0]]!! + getPlayableTrackUseCase(trackPool)!!
        updatedCards[teams[2]] = updatedCards[teams[2]]!! + listOf(
            getPlayableTrackUseCase(trackPool)!!,
            getPlayableTrackUseCase(trackPool)!!
        )

        val updatedGame = initialGame.copy(collectedCardsByTeam = updatedCards)
        saveGameUseCase(updatedGame)

        viewModel = LeaderBoardViewModel(getGameUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(3, state.pointsByTeam.size)

        val expectedOrder = listOf(teams[2], teams[0], teams[1]) // Team 3 (3), Team 1 (2), Team 2 (1)
        assertEquals(expectedOrder, state.pointsByTeam.keys.toList())

        assertEquals(3, state.pointsByTeam[teams[2]])
        assertEquals(2, state.pointsByTeam[teams[0]])
        assertEquals(1, state.pointsByTeam[teams[1]])
    }

    @Test
    fun `init - handles null game gracefully`() = testScope.runTest {
        viewModel = LeaderBoardViewModel(getGameUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.pointsByTeam.isEmpty())
    }

    @Test
    fun `onEvent - OnContinueOrCloseButtonClick emits NavigateBack effect`() = testScope.runTest {
        viewModel = LeaderBoardViewModel(getGameUseCase)
        advanceUntilIdle()

        val emittedEffects = mutableListOf<LeaderBoardEffect>()
        val job = launch {
            viewModel.effect.collect { effect ->
                emittedEffects.add(effect)
            }
        }

        viewModel.onEvent(LeaderBoardEvent.OnContinueOrCloseButtonClick)
        advanceUntilIdle()

        assertTrue(emittedEffects.contains(LeaderBoardEffect.NavigateBack))

        job.cancel()
    }
}