package com.rdisoftware.chronobeat.domain.models

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Game(
    val id: Uuid,
    val teams: List<Team>,
    val currentTeam: Team,
    val collectedCardsByTeam: Map<Team, List<Track>>,
    val playlistId: String
)
