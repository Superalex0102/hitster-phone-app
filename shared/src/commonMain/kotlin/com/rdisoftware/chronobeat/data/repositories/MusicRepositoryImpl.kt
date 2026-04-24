package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class MusicRepositoryImpl : MusicRepository {
    override suspend fun getUserPlaylists() {
        TODO("Not yet implemented")
    }

    override suspend fun getChronobeatPlaylists() {
        TODO("Not yet implemented")
    }

    override suspend fun getTrackInfo(trackId: String): Track {
        TODO("Not yet implemented")
    }

    override suspend fun playMusic() {
        TODO("Not yet implemented")
    }

    override suspend fun pauseMusic() {
        TODO("Not yet implemented")
    }
}