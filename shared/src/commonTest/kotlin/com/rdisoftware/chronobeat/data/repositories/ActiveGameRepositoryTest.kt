package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class ActiveGameRepositoryTest {

    private lateinit var repository: ActiveGameRepository

    @BeforeTest
    fun setup() {
        repository = ActiveGameRepositoryImpl()
    }

    private fun createDummyGame(): Game {
        val dummyTeam = Team(id = Uuid.random(), name = "Test Team", color = TeamColor.CRIMSON)

        return Game(
            id = Uuid.random(),
            teams = listOf(dummyTeam),
            currentTeam = dummyTeam,
            collectedCardsByTeam = emptyMap(),
            playlistId = "test_playlist_id_123"
        )
    }

    @Test
    fun `getGame should return null initially`() = runTest {
        assertNull(repository.getGame(), "Game should be null when initialized")
    }

    @Test
    fun `saveGame should store the game correctly`() = runTest {
        val testGame = createDummyGame()

        repository.saveGame(testGame)

        val fetchedGame = repository.getGame()
        assertEquals(testGame, fetchedGame)
    }

    @Test
    fun `clearGame should remove the stored game`() = runTest {
        val testGame = createDummyGame()
        repository.saveGame(testGame)

        repository.clearGame()

        assertNull(repository.getGame(), "Game should be null after clearing")
    }

    @Test
    fun `observeGame should emit state changes sequentially`() = runTest {
        val states = mutableListOf<Game?>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.observeGame().toList(states)
        }

        val game1 = createDummyGame()
        val game2 = createDummyGame().copy(playlistId = "another_playlist_id")

        repository.saveGame(game1)
        repository.saveGame(game2)
        repository.clearGame()

        assertEquals(4, states.size)
        assertNull(states[0])
        assertEquals(game1, states[1])
        assertEquals(game2, states[2])
        assertNull(states[3])

        job.cancel()
    }
}