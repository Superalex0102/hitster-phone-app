package com.rdisoftware.chronobeat.presentation.preview

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Team
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object MockGameData {

    private val teams = listOf(
        Team(id = Uuid.random(), name = "Team One", color = TeamColor.entries[0]),
        Team(id = Uuid.random(), name = "Team Two", color = TeamColor.entries[1]),
        Team(id = Uuid.random(), name = "Team Three", color = TeamColor.entries[2]),
        Team(id = Uuid.random(), name = "Team Four", color = TeamColor.entries[3]),
    )

    val game = Game(
        id = Uuid.random(),
        teams = teams,
        currentTeam = teams.first(),
        collectedCardsByTeam = teams.associateWith { emptyList() },
        playlistId = "fake_playlist_id"
    )
}