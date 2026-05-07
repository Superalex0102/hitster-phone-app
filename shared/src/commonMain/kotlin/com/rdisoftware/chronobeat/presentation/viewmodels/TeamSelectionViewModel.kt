package com.rdisoftware.chronobeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.presentation.team_selection.TeamSelectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TeamSelectionViewModel(
    // TODO: Inject usecases here
    // private val addTeamUseCase: AddTeamUseCase,
    // private val deleteTeamUseCase: DeleteTeamUseCase,
    // private val updateTeamUseCase: UpdateTeamUseCase,
    // private val getTeamsUseCase: GetTeamsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TeamSelectionState())
    val state = _state.asStateFlow()

    fun onNameChanged(newName: String) {
        if (newName.length <= 16) {
            _state.update { it.copy(inputName = newName) }
        }
    }

    fun onColorChanged(newColor: TeamColor) {
        _state.update { it.copy(selectedColor = newColor) }
    }

    fun addTeam() {
        viewModelScope.launch {
            // TODO: Replace with addTeamUseCase(team)
            val currentState = _state.value
            if (currentState.canAddTeam) {
                val newTeam = Team(
                    id = Uuid.random(),
                    name = currentState.inputName.trim(),
                    color = currentState.selectedColor
                )
                _state.update { oldState ->
                    val updatedTeams = oldState.teams + newTeam
                    oldState.copy(
                        teams = updatedTeams,
                        inputName = "",
                        selectedColor = TeamColor.entries.firstOrNull { color ->
                            updatedTeams.none { it.color == color }
                        } ?: TeamColor.entries.first()
                    )
                }
            }
        }
    }

    fun deleteTeam(teamId: Uuid) {
        viewModelScope.launch {
            // TODO: Replace with deleteTeamUseCase(teamId)
            _state.update { oldState ->
                oldState.copy(
                    teams = oldState.teams.filterNot { it.id == teamId }
                )
            }
        }
    }

    fun updateTeam(updatedTeam: Team) {
        viewModelScope.launch {
            // TODO: Replace with updateTeamUseCase(updatedTeam)
            _state.update { oldState ->
                oldState.copy(
                    teams = oldState.teams.map {
                        if (it.id == updatedTeam.id) updatedTeam else it
                    }
                )
            }
        }
    }

    fun startEdit(team: Team) {
        _state.update { it.copy(
            editingTeam = team,
            inputName = team.name
        )}
    }

    fun confirmEdit() {
        val currentState = _state.value
        val editing = currentState.editingTeam ?: return
        updateTeam(editing.copy(name = currentState.inputName))
        _state.update { it.copy(editingTeam = null, inputName = "") }
    }
}