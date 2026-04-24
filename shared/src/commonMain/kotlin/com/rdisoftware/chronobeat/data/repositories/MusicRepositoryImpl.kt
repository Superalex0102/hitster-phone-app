package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.mappers.toDomain
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.remote.api.TokenService
import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class MusicRepositoryImpl(
    private val chronoBeatApi: ChronoBeatApi,
    private val tokenService: TokenService
) : MusicRepository {
    override suspend fun getUserPlaylists() {
        TODO("Not yet implemented")
    }

    override suspend fun getChronobeatPlaylists() : List<Playlist>{
        return chronoBeatApi.fetchChronoBeatPlaylists().toDomain()

    }

    override suspend fun getTrackInfo(trackId: String): Track {
        val token = tokenService.getAccessToken()
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