package com.rdisoftware.chronobeat.data.mappers

import com.rdisoftware.chronobeat.data.remote.dto.ChronoBeatPlaylistResponseDto
import com.rdisoftware.chronobeat.data.remote.dto.MusicCardDto
import com.rdisoftware.chronobeat.domain.models.MusicCard
import com.rdisoftware.chronobeat.domain.models.Playlist

class MusicMapper {
    fun MusicCardDto.toDomain(): MusicCard {
        return MusicCard(
            id = this.id,
            title = this.name,
            mainArtist = this.artists[0].name,
            featArtists = this.artists.drop(1).map { it.name },
            releaseYear = this.albumDto.releaseDate.split("-")[0].toInt(),
            isPlayable = this.isPlayable
        )
    }

    fun ChronoBeatPlaylistResponseDto.toDomain(): Playlist {
        return Playlist(
            id = this.playlists[0].id,
            name = this.playlists[0].name,
            trackIds = this.playlists[0].trackIds
        )
    }
}