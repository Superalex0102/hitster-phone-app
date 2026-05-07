package com.rdisoftware.chronobeat.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChronoBeatPlaylistDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("tracks")
    val trackIds: List<String>
)

@Serializable
data class ChronoBeatPlaylistResponseDto(
    @SerialName("playlists")
    val playlists: List<ChronoBeatPlaylistDto>
)