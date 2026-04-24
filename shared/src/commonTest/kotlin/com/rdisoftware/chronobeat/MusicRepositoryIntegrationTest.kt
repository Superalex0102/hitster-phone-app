package com.rdisoftware.chronobeat

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class MusicRepositoryIntegrationTest {
    val realTokenManager = TokenManager()
    val realClient = HttpClient {
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


    val realApi = ChronoBeatApi(realTokenManager, realClient)

    val repository = MusicRepositoryImpl(realApi)

    @Test
    fun getTrackInfo() {
        runBlocking {
            val testTrackId = "4PTG3Z6ehGkBFwjybzWkR8"

            val track = repository.getTrackInfo(testTrackId)

            println("Successfully fetched and converted: ${track}")

            assertEquals(testTrackId, track.id)
            assertEquals("Rick Astley", track.mainArtist)
            assertEquals("Never Gonna Give You Up", track.title)
            assertEquals(1987, track.releaseYear)
            assertEquals(0, track.featArtists.size)

        }
    }

    @Test
    fun getChronoBeatPlaylists() {
        runBlocking {
            val playlists = repository.getChronobeatPlaylists()

            println("Successfully fetched and converted: ${playlists}")

            assertEquals(2, playlists.size)
            assertEquals(938, playlists.first { it.name == "Legjobb Magyar Zenék" }.trackIds.size)
            assertContains(playlists.map { it.name }, "Legjobb Magyar Zenék")
        }
    }
}