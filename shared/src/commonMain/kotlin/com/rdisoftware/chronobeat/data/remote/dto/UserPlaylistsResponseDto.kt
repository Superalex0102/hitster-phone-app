package com.rdisoftware.chronobeat.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSummaryDto(
    val id: String,
    val name: String,
    @SerialName("total")
    val trackCount: Int
)

@Serializable
data class UserPlaylistsResponseDto(
    @SerialName("items")
    val playlists: List<PlaylistSummaryDto>,
)
