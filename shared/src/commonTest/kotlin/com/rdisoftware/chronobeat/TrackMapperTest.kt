package com.rdisoftware.chronobeat

import com.rdisoftware.chronobeat.data.mappers.toDomain
import com.rdisoftware.chronobeat.data.remote.dto.AlbumDto
import com.rdisoftware.chronobeat.data.remote.dto.ArtistDto
import com.rdisoftware.chronobeat.data.remote.dto.TrackDetailDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TrackMapperTest {

    @Test
    fun `toDomain maps basic fields correctly`() {
        val dto = TrackDetailDto(
            id = "abc123",
            name = "Never Gonna Give You Up",
            artists = listOf(ArtistDto("Rick Astley")),
            albumDto = AlbumDto(releaseDate = "1987-07-27"),
            isPlayable = true
        )
        val track = dto.toDomain()
        assertEquals("abc123", track.id)
        assertEquals("Never Gonna Give You Up", track.title)
        assertEquals("Rick Astley", track.mainArtist)
        assertEquals(1987, track.releaseYear)
        assertTrue(track.isPlayable)
    }

    @Test
    fun `toDomain first artist becomes mainArtist`() {
        val dto = makeTrackDto(artists = listOf(ArtistDto("Rick Astley"), ArtistDto("DJ Bob")))
        assertEquals("Rick Astley", dto.toDomain().mainArtist)
    }

    @Test
    fun `toDomain remaining artists become featArtists`() {
        val dto = makeTrackDto(artists = listOf(ArtistDto("Rick Astley"), ArtistDto("DJ Bob"), ArtistDto("MC Jane")))
        assertEquals(listOf("DJ Bob", "MC Jane"), dto.toDomain().featArtists)
    }

    @Test
    fun `toDomain single artist results in empty featArtists`() {
        val dto = makeTrackDto(artists = listOf(ArtistDto("Rick Astley")))
        assertEquals(emptyList(), dto.toDomain().featArtists)
    }

    @Test
    fun `toDomain parses year from full date`() {
        val dto = makeTrackDto(releaseDate = "1987-07-27")
        assertEquals(1987, dto.toDomain().releaseYear)
    }

    @Test
    fun `toDomain parses year-only release date`() {
        val dto = makeTrackDto(releaseDate = "1987")
        assertEquals(1987, dto.toDomain().releaseYear)
    }

    @Test
    fun `toDomain throws when artists list is empty`() {
        val dto = makeTrackDto(artists = emptyList())
        assertFailsWith<IndexOutOfBoundsException> {
            dto.toDomain()
        }
    }

    @Test
    fun `toDomain throws when release date is invalid`() {
        val dto = makeTrackDto(releaseDate = "invalid-date")
        assertFailsWith<NumberFormatException> {
            dto.toDomain()
        }
    }

    private fun makeTrackDto(
        id: String = "abc123",
        name: String = "Never Gonna Give You Up",
        artists: List<ArtistDto> = listOf(ArtistDto("Rick Astley")),
        releaseDate: String = "1987-07-27",
        isPlayable: Boolean = true
    ) = TrackDetailDto(
        id = id,
        name = name,
        artists = artists,
        albumDto = AlbumDto(releaseDate = releaseDate),
        isPlayable = isPlayable
    )
}