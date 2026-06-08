package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.usecases.game.ClearGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.constants.TeamSelectionConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TeamSelectionState(
    val teams: List<Team> = emptyList(),
    val inputName: String = "",
    val selectedColor: TeamColor = TeamColor.entries.first(),
    val editingTeam: Team? = null
) {
    val canStartGame: Boolean = teams.size >= TeamSelectionConstants.MIN_TEAMS

    val canAddTeam: Boolean =
        teams.size < TeamSelectionConstants.MAX_TEAMS && inputName.isNotBlank() && inputName.length <= TeamSelectionConstants.MAX_NAME_LENGTH

    val isMaxReached: Boolean = teams.size >= TeamSelectionConstants.MAX_TEAMS
    val isEditing: Boolean = editingTeam != null
}

@OptIn(ExperimentalUuidApi::class)
class TeamSelectionViewModel(
    private val addTeamUseCase: AddTeamUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    private val updateTeamUseCase: UpdateTeamUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val clearGameUseCase: ClearGameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TeamSelectionState())
    val state = _state.asStateFlow()

    init {
        loadTeams()
    }

    fun onNameChanged(newName: String) {
        if (newName.length <= TeamSelectionConstants.MAX_NAME_LENGTH) {
            _state.update { it.copy(inputName = newName) }
        }
    }

    fun onColorChanged(newColor: TeamColor) {
        _state.update { it.copy(selectedColor = newColor) }
    }

    fun addTeam() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.canAddTeam) {
                val name = currentState.inputName.trim()
                val color = currentState.selectedColor

                addTeamUseCase(name, color)
                _state.update { it.copy(inputName = "") }
                loadTeams()
            }
        }
    }

    fun deleteTeam(teamId: Uuid) {
        viewModelScope.launch {
            deleteTeamUseCase(teamId)
            loadTeams()
        }
    }

    fun updateTeam(updatedTeam: Team) {
        viewModelScope.launch {
            updateTeamUseCase(updatedTeam)
            loadTeams()
        }
    }

    fun startEdit(team: Team) {
        _state.update {
            it.copy(
                editingTeam = team,
                inputName = team.name
            )
        }
    }

    fun confirmEdit() {
        val currentState = _state.value
        val editing = currentState.editingTeam ?: return
        updateTeam(editing.copy(name = currentState.inputName.trim()))
        _state.update { it.copy(editingTeam = null, inputName = "") }
    }

    fun clearGame() {
        viewModelScope.launch {
            clearGameUseCase()
        }
    }

    private fun loadTeams() {
        viewModelScope.launch {
            val updatedTeams = getTeamsUseCase()
            _state.update { oldState ->
                oldState.copy(
                    teams = updatedTeams,
                    selectedColor = TeamColor.entries.firstOrNull { color ->
                        updatedTeams.none { it.color == color }
                    } ?: TeamColor.entries.first()
                )
            }
        }
    }
}