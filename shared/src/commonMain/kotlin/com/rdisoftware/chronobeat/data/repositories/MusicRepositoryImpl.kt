package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.mappers.toDomain
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class MusicRepositoryImpl(
    private val chronoBeatApi: ChronoBeatApi
) : MusicRepository {
    override suspend fun getUserPlaylistsSummary(): List<PlaylistSummary> {
        return try {
            chronoBeatApi.fetchUserPlaylists().toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch user's playlists", e)
        }
    }

    override suspend fun getUserPlaylistDetails(playlistId: String): Playlist {
        return try {
            chronoBeatApi.fetchTracksFromPlaylist(playlistId).toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch user's playlists", e)
        }
    }

    override suspend fun getChronobeatPlaylists() : List<Playlist>{
        return try {
            chronoBeatApi.fetchChronoBeatPlaylists().toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch ChronoBeat playlists", e)
        }

    }

    override suspend fun getTrackInfo(trackId: String): Track {
        return try {
            chronoBeatApi.fetchTrack(trackId).toDomain()
        } catch (e: Exception) {
            throw RuntimeException("Couldn't fetch track info for trackId: $trackId", e)
        }
    }

    override suspend fun playMusic() {
        TODO("Not yet implemented")
    }

    override suspend fun pauseMusic() {
        TODO("Not yet implemented")
    }
}