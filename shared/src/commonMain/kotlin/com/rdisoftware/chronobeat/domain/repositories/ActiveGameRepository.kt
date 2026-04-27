package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.domain.models.Game
import kotlinx.coroutines.flow.Flow

interface ActiveGameRepository {
    suspend fun getGame(): Game?
    suspend fun saveGame(game: Game)
    suspend fun clearGame()

    fun observeGame(): Flow<Game?>
}