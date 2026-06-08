package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.usecases.game.GetWinnerTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class GameSummaryUiState(
    val gameWinner: Team? = null
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

            updateWinnerTeam(team = getWinnerTeamUseCase())
        }
    }

    private fun updateWinnerTeam(team: Team?) {
        _state.update {
            it.copy(gameWinner = team)
        }
    }
}