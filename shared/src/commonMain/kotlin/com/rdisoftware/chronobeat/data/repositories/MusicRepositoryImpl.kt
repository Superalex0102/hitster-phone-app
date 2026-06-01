package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.mappers.toDomain
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.player.SpotifyPlayerController
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.shared.BuildConfig
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

class MusicRepositoryImpl(
    private val chronoBeatApi: ChronoBeatApi,
    private val spotifyPlayer: SpotifyPlayerController
) : MusicRepository {
    private suspend fun ensureValidToken() {
        if (!hasValidToken()) {
            throw RuntimeException("No valid token")
        }
    }

    override suspend fun getUserPlaylistsSummary(): List<PlaylistSummary> {
        return try {
            ensureValidToken()
            chronoBeatApi.fetchUserPlaylists().toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch user's playlists", e)
        }
    }

    override suspend fun getUserPlaylistDetails(playlistId: String): Playlist {
        return try {
            ensureValidToken()
            chronoBeatApi.fetchTracksFromPlaylist(playlistId).toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch user's playlists", e)
        }
    }

    override suspend fun getChronobeatPlaylists(): List<Playlist> {
        return try {
            chronoBeatApi.fetchChronoBeatPlaylists().toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch ChronoBeat playlists", e)
        }
    }

    override suspend fun getTrackInfo(trackId: String): Track {
        return try {
            ensureValidToken()
            chronoBeatApi.fetchTrack(trackId).toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch track info for trackId: $trackId", e)
        }
    }

    override suspend fun playMusic(trackId: String) {
        spotifyPlayer.playTrack(trackId)
    }

    override suspend fun resumeMusic() {
        spotifyPlayer.resume()
    }

    override suspend fun pauseMusic() {
        spotifyPlayer.pause()
    }

    override fun hasValidToken(): Boolean {
        return TokenManager.accessToken != null &&
                TokenManager.accessToken != BuildConfig.SPOTIFY_ACCESS_TOKEN_DEBUG

    }

    override suspend fun authenticate() {

        spotifyPlayer.authenticate()

        val token = withTimeoutOrNull(60_000L) {
            TokenManager.accessTokenFlow
                .filterNotNull()
                .first()
        }

        if (token == null) {
            throw RuntimeException("Spotify login timeout")
        }

    }
}