package com.rdisoftware.chronobeat.domain.usecases.game

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SaveGameUseCase(private val activeGameRepository: ActiveGameRepository) {
    suspend operator fun invoke(game: Game) {
        val gameDto = GameDto(
            id = game.id,
            teamIds = game.teams.map { it.id },
            currentTeamId = game.currentTeam.id,
            currentTrackId = game.currentTrack.id,
            collectedCardIdsByTeamId = game.collectedCardsByTeam.map { (team, tracks) ->
                team.id to tracks.map { it.id }
            }.toMap(),
            playlistId = game.playlistId,
            winnerTeamId = game.winnerTeam?.id
        )

        activeGameRepository.saveGame(gameDto)
    }
}