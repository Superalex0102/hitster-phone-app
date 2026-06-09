package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.usecases.game.GetWinnerTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameSummaryUiState(
    val gameWinner: Team? = null,
    val error: String? = null
)

class GameSummaryViewModel(
    private val getWinnerTeamUseCase: GetWinnerTeamUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(GameSummaryUiState())
    val state = _state.asStateFlow()

    init {
        getWinnerTeam()
    }

    private fun getWinnerTeam() {
        viewModelScope.launch {
            try {
                val winnerTeam = getWinnerTeamUseCase()
                _state.update {
                    it.copy(gameWinner = winnerTeam)
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to get winner: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}