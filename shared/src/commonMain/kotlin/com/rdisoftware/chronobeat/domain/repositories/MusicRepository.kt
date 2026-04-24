package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.models.Track

interface MusicRepository {
    suspend fun getUserPlaylists()
    suspend fun getChronobeatPlaylists()
    suspend fun getTrackInfo(trackId: String): Track
    suspend fun playMusic()
    suspend fun pauseMusic()
}