package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Stable
data class HomeUiState(
    val showSettingsPopup: Boolean = false,
    val isLocalEnabled: Boolean = true,
    val isOnlineEnabled: Boolean = false,
    val showLoginPopup: Boolean = true
)

sealed interface HomeEvent {
    data object OnSettingsClick : HomeEvent
    data object OnSettingsDismiss : HomeEvent
    data object OnOnlineGameClick : HomeEvent
    data object OnLoginPopupDismiss : HomeEvent
}

class HomeViewModel(): ViewModel() {

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
                    it.copy( showLoginPopup = false)
                }
            }
        }
    }
}