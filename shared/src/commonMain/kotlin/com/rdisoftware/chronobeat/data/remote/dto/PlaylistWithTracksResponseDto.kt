package com.rdisoftware.chronobeat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistWithTracksResponseDto(
    val id: String,
    val name: String,
    val tracks: TracksPagingDto
)

@Serializable
data class TracksPagingDto(
    val items: List<TrackItemWrapperDto>
)

@Serializable
data class TrackItemWrapperDto(
    val track: TrackIdDto
)

@Serializable
data class TrackIdDto(
    val id: String
)