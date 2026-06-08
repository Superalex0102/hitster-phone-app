package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class GameSummaryUiState(
    val gameWinner: Team? = null,
    val error: String? = null
)

class GameSummaryViewModel(
    //TODO: UseCase implementation
    //private val getWinnerTeamUseCase: GetWinnerTeamUseCase
): ViewModel() {
    private val _state = MutableStateFlow(GameSummaryUiState())
    val state = _state.asStateFlow()

    //Placeholder test team
    @OptIn(ExperimentalUuidApi::class)
    val testTeam = Team(
        id = Uuid.random(),
        name = "Droidok",
        color = TeamColor.TEAL
    )

    init {
        getWinnerTeam()
    }

    private fun getWinnerTeam() {
        viewModelScope.launch {
            try {
                //TODO: getWinnerTeamUseCase which provides a team the gameWinner UiState can be updated with.
                updateWinnerTeam(team = testTeam)
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to get winner: ${e.message}") }
            }
        }
    }

    private fun updateWinnerTeam(team: Team?) {
        _state.update {
            it.copy(gameWinner = team, error = null)
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}