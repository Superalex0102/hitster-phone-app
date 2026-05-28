package com.rdisoftware.chronobeat.domain.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Game(
    val id: Uuid,
    val teams: List<Team>,
    val currentTeam: Team,
    val currentTrack: Track,
    val collectedCardsByTeam: Map<Team, List<Track>>,
    val playlistId: String,
    val winnerTeam: Team?
)