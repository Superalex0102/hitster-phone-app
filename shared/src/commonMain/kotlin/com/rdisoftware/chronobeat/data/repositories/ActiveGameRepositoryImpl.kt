package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActiveGameRepositoryImpl : ActiveGameRepository {
    private val _game = MutableStateFlow<Game?>(null)


    override suspend fun getGame(): Game? {
        return _game.value
    }

    override suspend fun saveGame(game: Game) {
        _game.value = game
    }

    override suspend fun clearGame() {
        _game.value = null
    }

    override fun observeGame(): Flow<Game?> {
        return _game.asStateFlow()
    }

}