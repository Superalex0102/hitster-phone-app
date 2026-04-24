package com.rdisoftware.chronobeat.data.mappers

import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.MusicCardDto
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.models.Playlist

fun MusicCardDto.toDomain(): Track {
    return Track(
        id = this.id,
        title = this.name,
        mainArtist = this.artists[0].name,
        featArtists = this.artists.drop(1).map { it.name },
        releaseYear = this.albumDto.releaseDate.split("-")[0].toInt(),
        isPlayable = this.isPlayable
    )
}

fun ChronoBeatPlaylistResponseDto.toDomain(): List<Playlist> {
    return this.playlists.map { playlist ->
        Playlist(
            id = playlist.id,
            name = playlist.name,
            trackIds = playlist.trackIds
        )
    }
}