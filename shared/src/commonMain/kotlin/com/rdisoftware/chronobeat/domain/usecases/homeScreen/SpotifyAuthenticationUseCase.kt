package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class SpotifyAuthenticationUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() {
        musicRepository.authenticate()
    }

}