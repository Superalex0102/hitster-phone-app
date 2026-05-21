package com.rdisoftware.chronobeat.domain.repositories

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import kotlinx.coroutines.flow.Flow

interface ActiveGameRepository {
    suspend fun getGame(): GameDto?
    suspend fun saveGame(game: GameDto)
    suspend fun clearGame()

    fun observeGame(): Flow<GameDto?>
}