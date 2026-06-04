package com.rdisoftware.chronobeat.domain.usecases.game

import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.presentation.constants.GameConstants
import kotlin.uuid.ExperimentalUuidApi

class ProcessCorrectGuessUseCase {
    @OptIn(ExperimentalUuidApi::class)
    operator fun invoke(game: Game, track: Track, position: Int): Game {
        val currentTeam = game.currentTeam
        val currentTimeline = game.collectedCardsByTeam[currentTeam] ?: emptyList()

        val updatedTimeline = currentTimeline.toMutableList().apply {
            add(position, track)
        }

        val newCollectedCards = game.collectedCardsByTeam.toMutableMap().apply {
            put(currentTeam, updatedTimeline)
        }

        val winner = if (updatedTimeline.size >= GameConstants.CARDS_TO_WIN) currentTeam else null

        return game.copy(
            collectedCardsByTeam = newCollectedCards,
            winnerTeam = winner
        )
    }
}