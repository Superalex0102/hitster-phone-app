package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.domain.player.FakeSpotifyPlayerController
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MusicRepositoryIntegrationTest {

    private val realClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR-API: $message")
                }
            }
            level = LogLevel.ALL
        }
    }

    private val realApi = ChronoBeatApi( realClient)

    private val fakePlayer = FakeSpotifyPlayerController()
    private val repository = MusicRepositoryImpl(realApi, fakePlayer)

    @BeforeTest
    fun setup() {
        //Put a working token here!
        TokenManager.accessToken = "BQBubU1fObARM4I8TTGhYIiwcod8UfjWtjQZIYZRZz4II9aLWOCF9_7QgXazyQ7d0rEg9U2kF7DyMHX1SK6W6ZzG__DySSi1PD-lslLREiFRGiObmi877nFMG_7U9Y21CwFue3xbecv6SZQkUG3BM03-Z2mp0ZhBIVku136Zu9E0m9o_BRA1pg_SUcEr0C0fVeLugaQjoynrDTDtFrJ2TVLoJAQsmuTgs1BmuEwuGx3Jf79lHnKKLTRwkPk7m0V_3Amklg"
    }

    @Test
    fun getTrackInfo() = runTest {
        val testTrackId = "4PTG3Z6ehGkBFwjybzWkR8"
        val track = repository.getTrackInfo(testTrackId)

        println("Successfully fetched and converted: $track")

        assertEquals(testTrackId, track.id)
        assertEquals("Rick Astley", track.mainArtist)
        assertEquals("Never Gonna Give You Up", track.title)
        assertEquals(1987, track.releaseYear)
        assertEquals(0, track.featArtists.size)
    }

    @Test
    fun getChronoBeatPlaylists() = runTest {
        val playlists = repository.getChronobeatPlaylists()

        println("Successfully fetched and converted: $playlists")

        assertTrue(playlists.isNotEmpty(), "The playlist can't be empty")

        val playlist = playlists.firstOrNull { it.name == "Legjobb Magyar Zenék" }
        assertTrue(playlist != null, "The playlist 'Legjobb Magyar Zenék' should be present")

        assertTrue(
            playlist.trackIds.size > 800,
            "The playlist 'Legjobb Magyar Zenék' should contain more than 800 tracks, but found ${playlist.trackIds.size}"
        )
    }
}