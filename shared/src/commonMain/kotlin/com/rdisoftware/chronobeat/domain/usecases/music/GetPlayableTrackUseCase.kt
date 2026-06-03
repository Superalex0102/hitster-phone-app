package com.rdisoftware.chronobeat.domain.usecases.music

import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class GetPlayableTrackUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(trackIdPool: MutableList<String>): Track? {
        while (trackIdPool.isNotEmpty()) {
            val nextId = trackIdPool.removeAt(0)
            runCatching {
                val track = musicRepository.getTrackInfo(nextId)
                if (track.isPlayable) return track
            }
        }
        return null
    }
}