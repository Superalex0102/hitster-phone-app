package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.repositories.TokenRepository

class MusicRepositoryImpl(
    // TODO: private val apiService: MusicApiService,
    private val tokenRepository: TokenRepository
) : MusicRepository {
    override suspend fun getUserPlaylists() {
        TODO("Not yet implemented")
    }

    override suspend fun getChronobeatPlaylists() {
        TODO("Not yet implemented")
    }

    override suspend fun getTrackInfo(trackId: String): Track {
        val token = tokenRepository.getAccessToken()
        // TODO: val response = apiService.fetchMusic(token)
       //return response.map { it.toDomain() }
    }

    override suspend fun playMusic() {
        TODO("Not yet implemented")
    }

    override suspend fun pauseMusic() {
        TODO("Not yet implemented")
    }
}