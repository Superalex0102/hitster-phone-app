package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.usecases.game.GetGameUseCase
import com.rdisoftware.chronobeat.presentation.enums.LeaderBoardMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



data class LeaderBoardUiState(
    val isLoading: Boolean = false
)

sealed interface LeaderBoardEvent {
    data object OnContinueOrCloseButtonClick : LeaderBoardEvent
}

sealed interface LeaderBoardEffect {
    data object NavigateBack: LeaderBoardEffect
}

class LeaderBoardViewModel(
    private val getGameUseCase: GetGameUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LeaderBoardUiState())
    val state: StateFlow<LeaderBoardUiState> = _state.asStateFlow()

    private val _effect = Channel<LeaderBoardEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LeaderBoardEvent) {
        when (event) {
            LeaderBoardEvent.OnContinueOrCloseButtonClick -> handleContinueOrCloseButtonClick()
        }
    }

    private fun handleContinueOrCloseButtonClick() {
        viewModelScope.launch {
            if (_state.value.isLoading) return@launch
            _state.update { it.copy(isLoading = true) }

            try {
                _effect.send(LeaderBoardEffect.NavigateBack)
            } catch (e: Exception) {
                println("Error handling button click: ${e.message}")
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}