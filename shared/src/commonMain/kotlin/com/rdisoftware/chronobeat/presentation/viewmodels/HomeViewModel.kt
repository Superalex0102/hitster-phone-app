package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.usecases.game.GetGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.AuthResult
import com.rdisoftware.chronobeat.domain.usecases.music.CheckSpotifyAuthUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.SpotifyAuthenticationUseCase
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.error_message
import com.rdisoftware.chronobeat.shared.resources.sing_in_to_spotify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@Stable
data class HomeUiState(
    val showSettingsPopup: Boolean = false,
    val isLocalEnabled: Boolean = true,
    val isOnlineEnabled: Boolean = false,
    val showLoginPopup: Boolean = false,
    val showResumePopup: Boolean = false,
    val loginMessage: StringResource? = null
)

sealed interface HomeEvent {
    data object OnSettingsClick : HomeEvent
    data object OnSettingsDismiss : HomeEvent
    data object OnOnlineGameClick : HomeEvent
    data object OnLoginPopupDismiss : HomeEvent
    data object OnSignInClick : HomeEvent
    data object OnResumeDiscard : HomeEvent

    data class OnLocalGameClick(val navigate: (shouldLoadGame: Boolean) -> Unit) : HomeEvent
    data class OnResumeConfirm(val navigate: (shouldLoadGame: Boolean) -> Unit) : HomeEvent
}

class HomeViewModel(
    private val getGameUseCase: GetGameUseCase,
    private val checkSpotifyAuthUseCase: CheckSpotifyAuthUseCase,
    private val spotifyAuthenticationUseCase: SpotifyAuthenticationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnSettingsClick -> {
                _state.update {
                    it.copy(showSettingsPopup = !it.showSettingsPopup)
                }
            }

            HomeEvent.OnSettingsDismiss -> {
                _state.update {
                    it.copy(showSettingsPopup = false)
                }
            }

            HomeEvent.OnOnlineGameClick -> {
                if (_state.value.isOnlineEnabled) {
                    // TODO: Navigation implementation goes here, future plan
                }
            }

            HomeEvent.OnLoginPopupDismiss -> {
                _state.update {
                    it.copy(showLoginPopup = false)
                }
            }

            is HomeEvent.OnLocalGameClick -> {
                event.navigate(false)
            }

            is HomeEvent.OnResumeConfirm -> {
                _state.update { it.copy(showResumePopup = false) }
                event.navigate(true)
            }

            is HomeEvent.OnResumeDiscard -> {
                _state.update { it.copy(showResumePopup = false) }
            }

            HomeEvent.OnSignInClick -> {
                viewModelScope.launch {
                    try {
                        spotifyAuthenticationUseCase()
                        _state.update { it.copy(showLoginPopup = false) }
                    } catch (e: Exception) {
                        println("Login failed: ${e.message}")
                    }
                    checkResumeGame()
                }
            }
        }
    }

    fun checkSpotifyAuthentication() {
        viewModelScope.launch {
            when (checkSpotifyAuthUseCase()) {
                AuthResult.Authenticated -> {
                    _state.update {
                        it.copy(
                            showLoginPopup = false,
                            isLocalEnabled = true,
                            loginMessage = null
                        )
                    }
                    checkResumeGame()
                }

                AuthResult.RequiresLogin -> {
                    _state.update {
                        it.copy(
                            showLoginPopup = true,
                            isLocalEnabled = true,
                            loginMessage = Res.string.sing_in_to_spotify
                        )

                    }
                }

                AuthResult.Error -> {
                    _state.update {
                        it.copy(
                            showLoginPopup = true,
                            isLocalEnabled = false,
                            loginMessage = Res.string.error_message
                        )
                    }
                }

            }
        }
    }

    private suspend fun checkResumeGame() {
        val savedGame = getGameUseCase()
        if (savedGame != null) {
            _state.update { it.copy(showResumePopup = true) }
        }
    }
}