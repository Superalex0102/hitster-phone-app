package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.domain.player.FakeSpotifyPlayerController
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MusicRepositoryTest {

    private lateinit var fakeSpotifyPlayer: FakeSpotifyPlayerController
    private lateinit var musicRepository: MusicRepositoryImpl

    @BeforeTest
    fun setup() {
        fakeSpotifyPlayer = FakeSpotifyPlayerController()

        val dummyApi = ChronoBeatApi(HttpClient())

        musicRepository = MusicRepositoryImpl(
            chronoBeatApi = dummyApi,
            spotifyPlayer = fakeSpotifyPlayer
        )

        TokenManager.accessToken = "test_dummy_token"
    }

    @Test
    fun `playMusic should call playTrack on SpotifyPlayerController with correct trackId`() = runTest {
        val expectedTrackId = "4PTG3Z6ehGkBFwjybzWkR8"
        musicRepository.playMusic(expectedTrackId)
        assertEquals(expectedTrackId, fakeSpotifyPlayer.playedTrackId)
    }

    @Test
    fun `pauseMusic should call pause on SpotifyPlayerController`() = runTest {
        musicRepository.pauseMusic()
        assertTrue(fakeSpotifyPlayer.isPausedCalled)
    }

    @Test
    fun `resumeMusic should call resume on SpotifyPlayerController`() = runTest {
        musicRepository.resumeMusic()
        assertTrue(fakeSpotifyPlayer.isResumedCalled)
    }
}