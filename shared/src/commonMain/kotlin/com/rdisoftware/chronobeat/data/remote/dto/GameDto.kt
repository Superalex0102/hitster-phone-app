package com.rdisoftware.chronobeat.data.remote.dto

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class GameDto(
    val id: Uuid,
    val teamIds: List<Uuid>,
    val currentTeamId: Uuid,
    val currentTrackId: String,
    val collectedCardIdsByTeamId: Map<Uuid, List<String>>,
    val playlistId: String,
    val winnerTeamId: Uuid?
)