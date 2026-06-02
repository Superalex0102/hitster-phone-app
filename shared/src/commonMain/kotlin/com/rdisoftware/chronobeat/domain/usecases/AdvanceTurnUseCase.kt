package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Track
import kotlin.uuid.ExperimentalUuidApi

class AdvanceTurnUseCase {
    @OptIn(ExperimentalUuidApi::class)
    operator fun invoke(game: Game, nextTrack: Track?): Game {
        val teams = game.teams
        val currentIndex = teams.indexOf(game.currentTeam)

        val nextTeam = teams[(currentIndex + 1) % teams.size]

        return game.copy(
            currentTeam = nextTeam,
            currentTrack = nextTrack ?: game.currentTrack
        )
    }
}