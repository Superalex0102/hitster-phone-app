package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.models.MusicCard

interface MusicRepository {
    suspend fun getUserPlaylists()
    suspend fun getChronobeatPlaylists()
    suspend fun getTrackInfo(trackId: String): MusicCard
    suspend fun playMusic()
    suspend fun pauseMusic()
}