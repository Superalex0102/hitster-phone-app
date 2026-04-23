package com.rdisoftware.chronobeat.domain.models

data class Playlist (
    val id: String,
    val name: String,
    val trackIds: List<String>
)