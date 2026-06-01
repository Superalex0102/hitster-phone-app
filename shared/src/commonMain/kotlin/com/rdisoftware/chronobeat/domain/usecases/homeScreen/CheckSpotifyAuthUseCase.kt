package com.rdisoftware.chronobeat.domain.usecases.homeScreen

import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.sing_in_to_spotify
import org.jetbrains.compose.resources.StringResource

sealed interface AuthResult {

    object Authenticated : AuthResult
    object RequiresLogin : AuthResult
    object Error : AuthResult

}

class CheckSpotifyAuthUseCase(
    private val musicRepository: MusicRepository
){
    suspend operator fun invoke(): AuthResult {
        return if (musicRepository.hasValidToken()) {
            AuthResult.Authenticated
        } else {
            AuthResult.RequiresLogin
        }
    }
}