package com.rdisoftware.chronobeat.domain.usecases.music

import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GetPlayableTrackUseCase(private val musicRepository: MusicRepository) {
    private val mutex = Mutex()
    suspend operator fun invoke(trackIdPool: MutableList<String>): Track? {
        while (true) {
            val nextId = mutex.withLock {
                if (trackIdPool.isNotEmpty()) trackIdPool.removeAt(0) else null
            }

            if (nextId == null) return null

            runCatching {
                val track = musicRepository.getTrackInfo(nextId)
                if (track.isPlayable) return track
            }
        }
    }
}