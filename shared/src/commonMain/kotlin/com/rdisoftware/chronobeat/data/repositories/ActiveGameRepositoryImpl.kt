package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActiveGameRepositoryImpl : ActiveGameRepository {
    private val _game = MutableStateFlow<GameDto?>(null)


    override suspend fun getGame(): GameDto? {
        return _game.value
    }

    override suspend fun saveGame(game: GameDto) {
        _game.value = game
    }

    override suspend fun clearGame() {
        _game.value = null
    }

    override fun observeGame(): Flow<GameDto?> {
        return _game.asStateFlow()
    }
}