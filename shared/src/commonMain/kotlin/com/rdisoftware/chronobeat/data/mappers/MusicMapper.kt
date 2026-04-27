package com.rdisoftware.chronobeat.data.mappers

import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.PlaylistWithTracksResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.TrackDetailDto
import com.rdisoftware.chronobeat.data.remote.dto.UserPlaylistsResponseDto
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary

fun TrackDetailDto.toDomain(): Track {
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

fun UserPlaylistsResponseDto.toDomain(): List<PlaylistSummary> {
    return this.playlists.map { playlist ->
        PlaylistSummary(
            id = playlist.id,
            name = playlist.name,
            trackCount = playlist.trackCount
        )
    }
}

fun PlaylistWithTracksResponseDto.toDomain(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        trackIds = this.tracks.items.map { it.track.id }
    )
}