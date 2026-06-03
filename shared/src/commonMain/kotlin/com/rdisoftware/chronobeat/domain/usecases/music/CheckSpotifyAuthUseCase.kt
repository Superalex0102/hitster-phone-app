package com.rdisoftware.chronobeat.domain.usecases.music

import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

sealed interface AuthResult {

    object Authenticated : AuthResult
    object RequiresLogin : AuthResult
    object Error : AuthResult

}

class CheckSpotifyAuthUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): AuthResult {
        return if (musicRepository.hasValidToken()) {
            AuthResult.Authenticated
        } else {
            AuthResult.RequiresLogin
        }
    }
}