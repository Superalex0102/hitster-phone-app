package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class GetChronobeatPlaylistsUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(): List<Playlist> {
        return musicRepository.getChronobeatPlaylists()
    }
}