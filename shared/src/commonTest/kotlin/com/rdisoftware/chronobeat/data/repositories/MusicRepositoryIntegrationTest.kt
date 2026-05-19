package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.domain.player.FakeSpotifyPlayerController
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MusicRepositoryIntegrationTest {
    private val mockTrackIds = (1..801).joinToString(",") { """{"track": {"id": "mock_track_$it"}}""" }

    private val mockEngine = MockEngine { request ->
        val url = request.url.toString()

        when {
            url.contains("tracks/4PTG3Z6ehGkBFwjybzWkR8") -> {
                respond(
                    content = """
                        {
                            "id": "4PTG3Z6ehGkBFwjybzWkR8",
                            "name": "Never Gonna Give You Up",
                            "is_playable": true,
                            "album": { "release_date": "1987-07-27" },
                            "artists": [ { "name": "Rick Astley" } ]
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            url.contains("database.json") -> {
                respond(
                    content = """
                        {
                            "id": "magyar_playlist_1",
                            "name": "Legjobb Magyar Zenék",
                            "tracks": {
                                "items": [ $mockTrackIds ]
                            }
                        }
                    """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            else -> respond("Not Found", HttpStatusCode.NotFound)
        }
    }

    private val mockClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    private val mockApi = ChronoBeatApi(mockClient)
    private val mockRepository = MusicRepositoryImpl(mockApi, FakeSpotifyPlayerController())

    @BeforeTest
    fun setup() {
        TokenManager.accessToken = "mock_github_actions_token"
    }

    @Test
    fun getTrackInfo() = runTest {
        val testTrackId = "4PTG3Z6ehGkBFwjybzWkR8"
        val track = mockRepository.getTrackInfo(testTrackId)

        println("Successfully fetched and converted mocked track: $track")

        assertEquals(testTrackId, track.id)
        assertEquals("Rick Astley", track.mainArtist)
        assertEquals("Never Gonna Give You Up", track.title)
        assertEquals(1987, track.releaseYear)
        assertEquals(0, track.featArtists.size)
    }

    private val realClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private val realApi = ChronoBeatApi(realClient)
    private val realRepository = MusicRepositoryImpl(realApi, FakeSpotifyPlayerController())

    @Test
    fun getChronoBeatPlaylists() = runTest {
        val playlists = realRepository.getChronobeatPlaylists()

        println("Successfully fetched and converted mocked playlists: ${playlists.size} items")

        assertTrue(playlists.isNotEmpty(), "The playlist can't be empty")

        val playlist = playlists.firstOrNull { it.name == "Legjobb Magyar Zenék" }
        assertTrue(playlist != null, "The playlist 'Legjobb Magyar Zenék' should be present")

        assertTrue(
            playlist.trackIds.size > 800,
            "The playlist 'Legjobb Magyar Zenék' should contain more than 800 tracks, but found ${playlist.trackIds.size}"
        )
    }
}