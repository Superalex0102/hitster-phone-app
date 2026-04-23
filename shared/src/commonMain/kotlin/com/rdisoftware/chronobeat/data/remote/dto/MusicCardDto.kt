package com.rdisoftware.chronobeat.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusicCardDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("artists")
    val artists: List<ArtistDto>,
    @SerialName("album")
    val albumDto: AlbumDto,
    @SerialName("is_playable")
    val isPlayable: Boolean
)

@Serializable
data class ArtistDto(
    @SerialName("name")
    val name: String
)

@Serializable
data class AlbumDto(
    @SerialName("release_date")
    val releaseDate: String
)