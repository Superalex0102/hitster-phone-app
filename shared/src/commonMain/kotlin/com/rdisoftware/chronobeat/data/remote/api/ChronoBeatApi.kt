package com.rdisoftware.chronobeat.data.remote.api

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.PlaylistWithTracksResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.TrackDetailDto
import com.rdisoftware.chronobeat.data.remote.dto.UserPlaylistsResponseDto
import com.rdisoftware.chronobeat.shared.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class ChronoBeatApi(
    private val tokenManager: TokenManager,
    private val httpClient: HttpClient
) {
    suspend fun fetchChronoBeatPlaylists() : ChronoBeatPlaylistResponseDto {
        return httpClient.get(BuildConfig.CHRONOBEAT_BASE_URL + "database.json").body()
    }

    suspend fun fetchUserPlaylists(limit: Int = 15, offset: Int = 0): UserPlaylistsResponseDto {
        val accessToken = tokenManager.getAccessToken()
        return httpClient.get(BuildConfig.SPOTIFY_BASE_URL + "me/playlists") {
            header("Authorization", "Bearer $accessToken")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()
    }

    suspend fun fetchTracksFromPlaylist(playlistId: String): PlaylistWithTracksResponseDto {
        val accessToken = tokenManager.getAccessToken()
        return httpClient.get(BuildConfig.SPOTIFY_BASE_URL + "playlists/$playlistId") {
            header("Authorization", "Bearer $accessToken")
            parameter("fiels", "id,name,tracks.total,tracks.items(track.id)")
        }.body()
    }
    suspend fun fetchTrack(trackId: String): TrackDetailDto {
        val accessToken = tokenManager.getAccessToken()
        return httpClient.get(BuildConfig.SPOTIFY_BASE_URL + "tracks/$trackId") {
            header("Authorization", "Bearer $accessToken")
        }.body()
    }
}