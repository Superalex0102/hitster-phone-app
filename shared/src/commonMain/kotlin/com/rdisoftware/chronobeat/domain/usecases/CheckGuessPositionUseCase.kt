package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.domain.models.Track

class CheckGuessPositionUseCase {
    operator fun invoke(timeline: List<Track>, track: Track, position: Int): Boolean {
        if (position < 0 || position > timeline.size) return false

        val before = if (position > 0) timeline.getOrNull(position - 1) else null
        val after = if (position < timeline.size) timeline.getOrNull(position) else null

        return (before == null || before.releaseYear <= track.releaseYear) &&
                (after == null || after.releaseYear >= track.releaseYear)
    }
}