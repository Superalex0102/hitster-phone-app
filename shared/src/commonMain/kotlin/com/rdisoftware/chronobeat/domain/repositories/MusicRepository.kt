package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary
import com.rdisoftware.chronobeat.domain.models.Track

interface MusicRepository {
    suspend fun getUserPlaylistsSummary(): List<PlaylistSummary>
    suspend fun getUserPlaylistDetails(playlistId: String): Playlist
    suspend fun getChronobeatPlaylists(): List<Playlist>
    suspend fun getTrackInfo(trackId: String): Track
    suspend fun playMusic()
    suspend fun pauseMusic()
}