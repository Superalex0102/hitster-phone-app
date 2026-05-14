package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class PlayMusicUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(trackId: String) {
        musicRepository.playMusic(trackId)
    }
}