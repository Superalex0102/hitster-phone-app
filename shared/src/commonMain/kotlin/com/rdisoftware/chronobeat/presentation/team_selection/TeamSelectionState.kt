package com.rdisoftware.chronobeat.presentation.team_selection

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team

data class TeamSelectionState(
    val teams: List<Team> = emptyList(),
    val inputName: String = "",
    val selectedColor: TeamColor = TeamColor.entries.first(),
    val editingTeam: Team? = null
) {
    val canStartGame: Boolean = teams.size >= 2

    val canAddTeam: Boolean = teams.size < 4 && inputName.isNotBlank() && inputName.length <= 16

    val isMaxReached: Boolean = teams.size >= 4
    val isEditing: Boolean = editingTeam != null
}