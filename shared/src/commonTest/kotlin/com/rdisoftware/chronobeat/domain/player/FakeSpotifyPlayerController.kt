package com.rdisoftware.chronobeat.domain.player

class FakeSpotifyPlayerController : SpotifyPlayerController {
    var playedTrackId: String? = null
    var isPausedCalled = false
    var isResumedCalled = false
    var isAuthenticateCalled = false

    override fun authenticate() {
        isAuthenticateCalled = true
    }

    override fun playTrack(trackId: String) {
        playedTrackId = trackId
    }

    override fun pause() {
        isPausedCalled = true
    }

    override fun resume() {
        isResumedCalled = true
    }

    override fun disconnect() {
        //no usage in tests
    }
}