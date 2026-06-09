package com.rdisoftware.chronobeat.domain.usecases.game

import com.rdisoftware.chronobeat.domain.models.Team

class GetWinnerTeamUseCase(
    private val getGameUseCase: GetGameUseCase
) {
    suspend operator fun  invoke(): Team? {
      val currentGame = getGameUseCase()
        return currentGame?.winnerTeam
    }
}