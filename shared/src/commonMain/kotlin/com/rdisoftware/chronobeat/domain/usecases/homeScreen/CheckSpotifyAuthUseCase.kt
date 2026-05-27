package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl

sealed interface AuthResult {
    object  Authenticated : AuthResult
    data class RequiresLogin(val message: String) : AuthResult
}

class CheckSpotifyAuthUseCase(
    private val musicRepository: MusicRepositoryImpl
){
    suspend operator fun invoke(): AuthResult {
        return try {
            musicRepository.getUserPlaylistsSummary()
            AuthResult.Authenticated
        } catch (e: Exception) {
            AuthResult.RequiresLogin("You must log in!")
        }
    }
}