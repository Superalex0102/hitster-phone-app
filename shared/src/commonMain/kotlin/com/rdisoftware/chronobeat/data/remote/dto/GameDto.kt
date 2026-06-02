package com.rdisoftware.chronobeat.data.remote.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
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