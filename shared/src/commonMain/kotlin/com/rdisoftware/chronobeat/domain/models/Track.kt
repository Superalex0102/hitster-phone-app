package com.rdisoftware.chronobeat.domain.models

data class Track(
    val id: String,
    val title: String,
    val mainArtist: String,
    val featArtists: List<String>,
    val releaseYear: Int,
    val isPlayable: Boolean
)