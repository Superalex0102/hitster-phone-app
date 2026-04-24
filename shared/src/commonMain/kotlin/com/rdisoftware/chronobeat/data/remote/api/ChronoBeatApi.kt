package com.rdisoftware.chronobeat.data.remote.api

import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistDto
import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChronoBeatApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun fetchChronoBeatPlaylists(): ChronoBeatPlaylistResponseDto {
        return httpClient.get("https://superalex0102.github.io/spotify-playlist-data/database.json").body()
    }
}