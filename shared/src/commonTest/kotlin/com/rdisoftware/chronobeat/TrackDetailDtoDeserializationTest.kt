package com.rdisoftware.chronobeat

import com.rdisoftware.chronobeat.data.mappers.toDomain
import com.rdisoftware.chronobeat.data.remote.dto.TrackDetailDto
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TrackDetailDtoDeserializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    private val realTrackDto: TrackDetailDto by lazy {
        json.decodeFromString(trackDetailResponseJson)
    }

    @Test
    fun `real Spotify JSON deserializes without crashing`() {
        assertNotNull(realTrackDto)
    }

    @Test
    fun `real Spotify JSON maps to correct track`() {
        val track = realTrackDto.toDomain()
        assertEquals("3kGn7euZy2g0Km0Bjxv9at", track.id)
        assertEquals("PRÉMIUM VIP GANG EXTRA POSSE EXKLUZÍV", track.title)
        assertEquals("6363", track.mainArtist)
        assertEquals(2024, track.releaseYear)
        assertTrue(track.isPlayable)
    }

    @Test
    fun `real Spotify JSON has correct feat artists`() {
        val track = realTrackDto.toDomain()
        assertEquals(6, track.featArtists.size)
        assertEquals(listOf("Krúbi", "Ketioz", "bongor", "Lil Frakk", "Ótvar Pestis", "Saiid"), track.featArtists)
    }
}