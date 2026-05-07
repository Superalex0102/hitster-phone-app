package com.rdisoftware.chronobeat.di

import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.domain.player.SpotifyPlayerController
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RepositoryFactory {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val api = ChronoBeatApi(httpClient)

    fun createMusicRepository(spotifyPlayer: SpotifyPlayerController): MusicRepositoryImpl {
        return MusicRepositoryImpl(
            chronoBeatApi = api,
            spotifyPlayer = spotifyPlayer
        )
    }
}