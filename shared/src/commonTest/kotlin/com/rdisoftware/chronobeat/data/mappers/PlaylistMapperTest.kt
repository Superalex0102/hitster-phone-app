package com.rdisoftware.chronobeat.data.mappers

import com.rdisoftware.chronobeat.data.remote.dto.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaylistMapperTest {
    @Test
    fun `ChronoBeatPlaylistResponseDto toDomain maps populated list correctly`() {
        val dto = ChronoBeatPlaylistResponseDto(
            playlists = listOf(
                ChronoBeatPlaylistDto(id = "p1", name = "Magyar Retro", trackIds = listOf("t1", "t2")),
                ChronoBeatPlaylistDto(id = "p2", name = "Rock", trackIds = emptyList())
            )
        )

        val domainList = dto.toDomain()

        assertEquals(2, domainList.size)
        assertEquals("p1", domainList[0].id)
        assertEquals("Magyar Retro", domainList[0].name)
        assertEquals(listOf("t1", "t2"), domainList[0].trackIds)
        assertEquals("p2", domainList[1].id)
        assertTrue(domainList[1].trackIds.isEmpty())
    }

    @Test
    fun `ChronoBeatPlaylistResponseDto toDomain handles empty playlists gracefully`() {
        val dto = ChronoBeatPlaylistResponseDto(playlists = emptyList())

        val domainList = dto.toDomain()

        assertTrue(domainList.isEmpty(), "Üres DTO listának üres Domain listát kell eredményeznie")
    }

    @Test
    fun `UserPlaylistsResponseDto toDomain maps populated list correctly`() {
        val dto = UserPlaylistsResponseDto(
            playlists = listOf(
                PlaylistSummaryDto(id = "u1", name = "My Favorites", trackCount = 42)
            )
        )

        val domainList = dto.toDomain()

        assertEquals(1, domainList.size)
        assertEquals("u1", domainList[0].id)
        assertEquals("My Favorites", domainList[0].name)
        assertEquals(42, domainList[0].trackCount)
    }

    @Test
    fun `UserPlaylistsResponseDto toDomain handles empty playlists gracefully`() {
        val dto = UserPlaylistsResponseDto(playlists = emptyList())

        val domainList = dto.toDomain()

        assertTrue(domainList.isEmpty(), "Üres felhasználói playlistek esetén üres listát kell visszaadni")
    }

    @Test
    fun `PlaylistWithTracksResponseDto toDomain extracts track IDs correctly`() {
        val dto = PlaylistWithTracksResponseDto(
            id = "pl123",
            name = "Workout",
            tracks = TracksPagingDto(
                items = listOf(
                    TrackItemWrapperDto(track = TrackIdDto(id = "track_A")),
                    TrackItemWrapperDto(track = TrackIdDto(id = "track_B"))
                )
            )
        )

        val domain = dto.toDomain()

        assertEquals("pl123", domain.id)
        assertEquals("Workout", domain.name)
        assertEquals(listOf("track_A", "track_B"), domain.trackIds)
    }

    @Test
    fun `PlaylistWithTracksResponseDto toDomain handles empty tracks list gracefully`() {
        val dto = PlaylistWithTracksResponseDto(
            id = "pl123",
            name = "Empty Playlist",
            tracks = TracksPagingDto(items = emptyList()) // Üres track lista
        )

        val domain = dto.toDomain()

        assertEquals("pl123", domain.id)
        assertTrue(domain.trackIds.isEmpty(), "Ha a playlistben nincs szám, a trackIds-nek üresnek kell lennie")
    }
}