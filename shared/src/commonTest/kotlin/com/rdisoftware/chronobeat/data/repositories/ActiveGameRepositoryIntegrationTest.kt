package com.rdisoftware.chronobeat.data.repositories

import FakeMusicRepository
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.FakeTeamRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.game.*
import com.rdisoftware.chronobeat.domain.usecases.music.*
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import com.rdisoftware.chronobeat.presentation.viewmodels.GamePhase
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
class ActiveGameRepositoryIntegrationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var settings: MapSettings

    // Repositories
    private lateinit var activeGameRepository: ActiveGameRepositoryImpl
    private lateinit var fakeTeamRepository: FakeTeamRepository
    private lateinit var fakeMusicRepository: FakeMusicRepository

    // UseCases
    private lateinit var getPlayableTrackUseCase: GetPlayableTrackUseCase
    private lateinit var setupInitialGameUseCase: SetupInitialGameUseCase
    private lateinit var checkGuessPositionUseCase: CheckGuessPositionUseCase
    private lateinit var processCorrectGuessUseCase: ProcessCorrectGuessUseCase
    private lateinit var advanceTurnUseCase: AdvanceTurnUseCase
    private lateinit var playMusicUseCase: PlayMusicUseCase
    private lateinit var getGameUseCase: GetGameUseCase
    private lateinit var saveGameUseCase: SaveGameUseCase
    private lateinit var getChronobeatPlaylistsUseCase: GetChronobeatPlaylistsUseCase
    private lateinit var getTeamsUseCase: GetTeamsUseCase

    private lateinit var viewModel: GameViewModel

    // Test Data
    private val team1 = Team(Uuid.random(), "Red Team", TeamColor.CRIMSON)
    private val team2 = Team(Uuid.random(), "Blue Team", TeamColor.TEAL)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settings = MapSettings()

        // Init Repositories
        activeGameRepository = ActiveGameRepositoryImpl(settings)
        fakeTeamRepository = FakeTeamRepository(mutableListOf(team1, team2))
        fakeMusicRepository = FakeMusicRepository()

        // Init UseCases
        getPlayableTrackUseCase = GetPlayableTrackUseCase(fakeMusicRepository)
        checkGuessPositionUseCase = CheckGuessPositionUseCase()
        processCorrectGuessUseCase = ProcessCorrectGuessUseCase()
        advanceTurnUseCase = AdvanceTurnUseCase()
        getTeamsUseCase = GetTeamsUseCase(fakeTeamRepository)
        saveGameUseCase = SaveGameUseCase(activeGameRepository)
        getGameUseCase = GetGameUseCase(activeGameRepository, fakeMusicRepository, getTeamsUseCase)
        setupInitialGameUseCase = SetupInitialGameUseCase(getPlayableTrackUseCase, activeGameRepository)
        getChronobeatPlaylistsUseCase = GetChronobeatPlaylistsUseCase(fakeMusicRepository)
        playMusicUseCase = PlayMusicUseCase(fakeMusicRepository)

        // ViewModel initialized in tests to allow custom pre-conditions if needed
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = GameViewModel(
            getPlayableTrackUseCase = getPlayableTrackUseCase,
            setupInitialGameUseCase = setupInitialGameUseCase,
            checkGuessPositionUseCase = checkGuessPositionUseCase,
            processCorrectGuessUseCase = processCorrectGuessUseCase,
            advanceTurnUseCase = advanceTurnUseCase,
            playMusicUseCase = playMusicUseCase,
            getGameUseCase = getGameUseCase,
            saveGameUseCase = saveGameUseCase,
            getChronobeatPlaylistsUseCase = getChronobeatPlaylistsUseCase,
            getTeamsUseCase = getTeamsUseCase
        )
    }

    // -------------------------------------------------------------------------
    // Initialization Flow
    // -------------------------------------------------------------------------

    @Test
    fun `init loads playlists, sets up new game and shows next team popup`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.game)
        assertEquals(GamePhase.SHOW_NEXT_TEAM_POPUP, state.currentPhase)
        assertEquals(team1.id, state.currentTeam?.id)
        assertNotNull(state.currentTrack)
    }

    // -------------------------------------------------------------------------
    // Gameplay Flow
    // -------------------------------------------------------------------------

    @Test
    fun `onPopupAcknowledgePressed changes phase to GUESSING and plays music`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()

        viewModel.onPopupAcknowledgePressed()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(GamePhase.GUESSING, state.currentPhase)
        assertEquals(state.currentTrack?.id, fakeMusicRepository.lastPlayedTrackId)
    }

    @Test
    fun `correct guess delays on SHOW_RESULT then advances turn and updates timeline`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.game)
        assertNotNull(viewModel.state.value.currentTrack)

        viewModel.onPopupAcknowledgePressed()
        advanceUntilIdle()

        assertEquals(GamePhase.GUESSING, viewModel.state.value.currentPhase)

        viewModel.onGuessPressed(1)

        runCurrent()

        assertEquals(GamePhase.SHOW_RESULT, viewModel.state.value.currentPhase)
        assertNotNull(viewModel.state.value.isGuessCorrect)

        advanceTimeBy(2000)
        runCurrent()

        val state = viewModel.state.value
        assertEquals(GamePhase.SHOW_NEXT_TEAM_POPUP, state.currentPhase)
        assertEquals(team2.id, state.currentTeam?.id)
    }

    @Test
    fun `incorrect guess delays on SHOW_RESULT then advances turn without updating timeline`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()

        viewModel.onPopupAcknowledgePressed()
        advanceUntilIdle()

        assertEquals(GamePhase.GUESSING, viewModel.state.value.currentPhase)

        val initialTrackCount = viewModel.state.value.currentCardCount

        viewModel.onGuessPressed(-1)

        runCurrent()

        assertEquals(GamePhase.SHOW_RESULT, viewModel.state.value.currentPhase)
        assertEquals(false, viewModel.state.value.isGuessCorrect)

        advanceTimeBy(2000)
        runCurrent()

        val state = viewModel.state.value
        assertEquals(GamePhase.SHOW_NEXT_TEAM_POPUP, state.currentPhase)
        assertEquals(team2.id, state.currentTeam?.id)

        val updatedGame = state.game!!
        val team1Timeline = updatedGame.collectedCardsByTeam[team1]!!
        assertEquals(initialTrackCount, team1Timeline.size)
    }

    // -------------------------------------------------------------------------
    // Winning Flow
    // -------------------------------------------------------------------------

    @Test
    fun `reaching required cards triggers GAME_OVER phase`() = runTest(testDispatcher) {
        createViewModel()
        advanceUntilIdle()

        val cardsNeeded = GameConstants.CARDS_TO_WIN

        for (i in 1 until cardsNeeded) {

            // --- TEAM 1 TURN ---
            assertEquals(team1.id, viewModel.state.value.currentTeam?.id)

            viewModel.onPopupAcknowledgePressed()
            advanceUntilIdle()

            val currentState = viewModel.state.value
            val currentTrack = currentState.currentTrack!!
            val timeline = currentState.timeline

            var correctPosition = 0
            for (pos in 0..timeline.size) {
                val beforeYear = if (pos > 0) timeline[pos - 1].releaseYear else Int.MIN_VALUE
                val afterYear = if (pos < timeline.size) timeline[pos].releaseYear else Int.MAX_VALUE

                if (currentTrack.releaseYear in beforeYear..afterYear) {
                    correctPosition = pos
                    break
                }
            }

            viewModel.onGuessPressed(correctPosition)

            runCurrent()
            advanceTimeBy(2000)
            runCurrent()

            if (viewModel.state.value.currentPhase == GamePhase.GAME_OVER) break

            // --- TEAM 2 TURN ---
            assertEquals(team2.id, viewModel.state.value.currentTeam?.id)

            viewModel.onPopupAcknowledgePressed()
            advanceUntilIdle()

            viewModel.onGuessPressed(-1)
            runCurrent()

            advanceTimeBy(2000)
            runCurrent()
        }

        val finalState = viewModel.state.value
        assertEquals(GamePhase.GAME_OVER, finalState.currentPhase)
        assertEquals(team1.id, finalState.game?.winnerTeam?.id)
    }
}