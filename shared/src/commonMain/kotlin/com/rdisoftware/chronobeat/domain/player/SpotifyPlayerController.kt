package com.rdisoftware.chronobeat.domain.player

interface SpotifyPlayerController {
    fun authenticate()
    fun playTrack(trackId: String)
    fun pause()
    fun resume()
    fun disconnect()
}