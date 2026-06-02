package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.russhwolf.settings.Settings
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
        val settings = Settings()
        repository = ActiveGameRepositoryImpl(settings)
    }
    private fun createDummyGameDto(): GameDto {
        val dummyTeamId = Uuid.random()

        return GameDto(
            id = Uuid.random(),
            teamIds = listOf(dummyTeamId),
            currentTeamId = dummyTeamId,
            collectedCardIdsByTeamId = emptyMap(),
            playlistId = "test_playlist_id_123",
            currentTrackId = "test_track_id_123",
            winnerTeamId = dummyTeamId
        )
    }

    @Test
    fun `getGame should return null initially`() = runTest {
        assertNull(repository.getGame(), "Game should be null when initialized")
    }

    @Test
    fun `saveGame should store the game correctly`() = runTest {
        val testGameDto = createDummyGameDto()

        repository.saveGame(testGameDto)

        val fetchedGame = repository.getGame()
        assertEquals(testGameDto, fetchedGame)
    }

    @Test
    fun `clearGame should remove the stored game`() = runTest {
        val testGameDto = createDummyGameDto()
        repository.saveGame(testGameDto)

        repository.clearGame()

        assertNull(repository.getGame(), "Game should be null after clearing")
    }

    @Test
    fun `observeGame should emit state changes sequentially`() = runTest {
        val states = mutableListOf<GameDto?>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.observeGame().toList(states)
        }

        val game1 = createDummyGameDto()
        val game2 = createDummyGameDto().copy(playlistId = "another_playlist_id")

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