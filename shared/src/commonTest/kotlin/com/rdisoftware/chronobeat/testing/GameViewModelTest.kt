package com.rdisoftware.chronobeat.testing

import FakeMusicRepository
import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.GetSavedGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.RestartGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.SaveGameProgressUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.GamePhase
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class GameViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: GameViewModel

    private lateinit var fakeMusicRepository: FakeMusicRepository
    private lateinit var settings: Settings
    private lateinit var activeGameRepository: ActiveGameRepositoryImpl
    private lateinit var teamRepository: TeamRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        activeGameRepository = ActiveGameRepositoryImpl(settings)
        fakeMusicRepository = FakeMusicRepository()
        val playMusicUseCase = PlayMusicUseCase(fakeMusicRepository)
        val getSavedGameUseCase = GetSavedGameUseCase(activeGameRepository)
        val restartGameUseCase = RestartGameUseCase(activeGameRepository,teamRepository)
        val saveGameProgressUseCase = SaveGameProgressUseCase(activeGameRepository)


        viewModel = GameViewModel(activeGameRepository, fakeMusicRepository, playMusicUseCase, getSavedGameUseCase,saveGameProgressUseCase,restartGameUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Init ---

    @Test
    fun `init - game and tracks are loaded`() = testScope.runTest {
        advanceUntilIdle()
        assertNotNull(viewModel.state.value.game)
        assertTrue(viewModel.state.value.tracks.isNotEmpty())
    }

    @Test
    fun `init - each team gets a starter track`() = testScope.runTest {
        advanceUntilIdle()
        val state = viewModel.state.value
        val game = state.game!!
        game.teams.forEach { team ->
            val teamTimeline = game.collectedCardsByTeam[team]
            assertNotNull(teamTimeline)
            assertTrue(teamTimeline.isNotEmpty())
        }
    }

    @Test
    fun `init - starter tracks are unique per team`() = testScope.runTest {
        advanceUntilIdle()
        val game = viewModel.state.value.game!!
        val starterTracks = game.teams.mapNotNull { game.collectedCardsByTeam[it]?.firstOrNull() }
        assertEquals(starterTracks.size, starterTracks.distinct().size)
    }

    @Test
    fun `init - currentTrack is set and not in any timeline`() = testScope.runTest {
        advanceUntilIdle()
        val state = viewModel.state.value
        val currentTrack = state.currentTrack
        assertNotNull(currentTrack)
        val allTimelineTracks = state.game!!.collectedCardsByTeam.values.flatten()
        assertFalse(allTimelineTracks.contains(currentTrack))
    }

    @Test
    fun `init - currentTeam is first team`() = testScope.runTest {
        advanceUntilIdle()
        val state = viewModel.state.value
        assertEquals(state.game!!.teams.first(), state.currentTeam)
        // Új fázis miatt ellenőrizzük, hogy Popup-pal indulunk-e
        assertEquals(GamePhase.SHOW_NEXT_TEAM_POPUP, state.currentPhase)
    }

    // --- onGuessPressed - correct ---

    @Test
    fun `correct guess - track added to current team timeline`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val currentTeam = state.game!!.currentTeam
        val timelineBefore = state.game.collectedCardsByTeam[currentTeam]!!.size

        val currentTrack = state.currentTrack!!
        val validPosition = findValidPosition(state.timeline, currentTrack)

        viewModel.onGuessPressed(validPosition)
        advanceUntilIdle()

        val timelineAfter = viewModel.state.value.game!!.collectedCardsByTeam[currentTeam]!!.size
        assertEquals(timelineBefore + 1, timelineAfter)
    }

    @Test
    fun `correct guess - isGuessCorrect is true`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val currentTrack = state.currentTrack!!
        val validPosition = findValidPosition(state.timeline, currentTrack)

        viewModel.onGuessPressed(validPosition)

        assertTrue(viewModel.state.value.isGuessCorrect == true)
        assertEquals(GamePhase.SHOW_RESULT, viewModel.state.value.currentPhase)

        advanceUntilIdle()
    }

    @Test
    fun `correct guess - currentTrack changes`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val trackBefore = state.currentTrack!!
        val validPosition = findValidPosition(state.timeline, trackBefore)

        viewModel.onGuessPressed(validPosition)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.currentTrack == trackBefore)
    }

    @Test
    fun `correct guess - team advances to next`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val teamBefore = state.currentTeam!!
        val validPosition = findValidPosition(state.timeline, state.currentTrack!!)

        viewModel.onGuessPressed(validPosition)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.currentTeam == teamBefore)
    }

    // --- onGuessPressed - wrong ---

    @Test
    fun `wrong guess - track not added to timeline`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val currentTeam = state.game!!.currentTeam
        val timelineBefore = state.game.collectedCardsByTeam[currentTeam]!!.size
        val invalidPosition = findInvalidPosition(state.timeline, state.currentTrack!!)

        viewModel.onGuessPressed(invalidPosition)
        advanceUntilIdle()

        val timelineAfter = viewModel.state.value.game!!.collectedCardsByTeam[currentTeam]!!.size
        assertEquals(timelineBefore, timelineAfter)
    }

    @Test
    fun `wrong guess - isGuessCorrect is false`() = testScope.runTest {
        advanceUntilIdle()
        viewModel.onPopupAcknowledgePressed()

        val state = viewModel.state.value
        val invalidPosition = findInvalidPosition(state.timeline, state.currentTrack!!)

        viewModel.onGuessPressed(invalidPosition)

        assertTrue(viewModel.state.value.isGuessCorrect == false)

        advanceUntilIdle()
    }

    // --- nextTeam ---

    @Test
    fun `nextTeam - wraps around after last team`() = testScope.runTest {
        advanceUntilIdle()
        val game = viewModel.state.value.game!!
        val teamCount = game.teams.size

        repeat(teamCount) {
            viewModel.onPopupAcknowledgePressed()
            val state = viewModel.state.value
            val validPosition = findValidPosition(state.timeline, state.currentTrack!!)
            viewModel.onGuessPressed(validPosition)
            advanceUntilIdle()
        }

        val firstTeam = game.teams.first()
        assertEquals(firstTeam, viewModel.state.value.currentTeam)
    }

    // --- isGameWon ---

    @Test
    fun `game is not won initially`() = testScope.runTest {
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isGameWon)
    }

    @Test
    fun `game stops when a team reaches CARDS_TO_WIN`() = testScope.runTest {
        advanceUntilIdle()

        var guessCount = 0
        while (!viewModel.state.value.isGameWon && guessCount < 100) {
            viewModel.onPopupAcknowledgePressed()

            val state = viewModel.state.value
            val currentTrack = state.currentTrack ?: break
            val validPosition = findValidPosition(state.timeline, currentTrack)

            viewModel.onGuessPressed(validPosition)
            advanceUntilIdle()
            guessCount++
        }

        assertTrue(viewModel.state.value.isGameWon)
        assertEquals(GamePhase.GAME_OVER, viewModel.state.value.currentPhase)
    }

    // --- currentCardCount ---

    @Test
    fun `currentCardCount reflects current team timeline size`() = testScope.runTest {
        advanceUntilIdle()
        val state = viewModel.state.value
        assertEquals(state.timeline.size, state.currentCardCount)
    }

    // --- drawNextTrack ---

    @Test
    fun `next track is not already in any timeline`() = testScope.runTest {
        advanceUntilIdle()
        val state = viewModel.state.value
        val allTimelineTracks = state.game!!.collectedCardsByTeam.values.flatten()
        assertFalse(allTimelineTracks.contains(state.currentTrack))
    }

    // --- Helpers ---

    private fun findValidPosition(timeline: List<Track>, track: Track): Int {
        for (i in 0..timeline.size) {
            val before = if (i > 0) timeline.getOrNull(i - 1) else null
            val after = if (i < timeline.size) timeline.getOrNull(i) else null
            val valid = (before == null || before.releaseYear <= track.releaseYear) &&
                    (after == null || after.releaseYear >= track.releaseYear)
            if (valid) return i
        }
        return 0
    }

    private fun findInvalidPosition(timeline: List<Track>, track: Track): Int {
        for (i in 0..timeline.size) {
            val before = if (i > 0) timeline.getOrNull(i - 1) else null
            val after = if (i < timeline.size) timeline.getOrNull(i) else null
            val valid = (before == null || before.releaseYear <= track.releaseYear) &&
                    (after == null || after.releaseYear >= track.releaseYear)
            if (!valid) return i
        }
        return timeline.size + 1
    }
}