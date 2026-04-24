package com.rdisoftware.chronobeat.data.remote.api

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.TrackDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChronoBeatApi(
    private val tokenManager: TokenManager,
    private val httpClient: HttpClient
) {
    suspend fun fetchChronoBeatPlaylists(): ChronoBeatPlaylistResponseDto {
        return httpClient.get("https://superalex0102.github.io/spotify-playlist-data/database.json").body()
    }

    suspend fun fetchTrack(trackId: String): TrackDto {
        val accessToken = tokenManager.getAccessToken()
        return httpClient.get("https://api.spotify.com/v1/tracks/$trackId") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }
}