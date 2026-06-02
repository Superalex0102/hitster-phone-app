package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.data.remote.dto.GameDto
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json


class ActiveGameRepositoryImpl(
    private val settings: Settings
) : ActiveGameRepository {
    private val _game = MutableStateFlow<GameDto?>(null)
    private val customJson = Json {
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true
    }

    init {
        val saveGameJson = settings.getStringOrNull("SAVED_GAME")
        if (saveGameJson != null) {
            _game.value = try {
                customJson.decodeFromString<GameDto>(saveGameJson)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getGame(): GameDto? {
        val savedGameJson = settings.getStringOrNull("SAVED_GAME")
        if (savedGameJson != null) {
            return try {
                val deserializedGame = customJson.decodeFromString<GameDto>(savedGameJson)
                _game.value = deserializedGame
                deserializedGame
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    override suspend fun saveGame(game: GameDto) {
        _game.value = game
        val jsonString = customJson.encodeToString(game)
        settings.putString("SAVED_GAME", jsonString)
    }

    override suspend fun clearGame() {
        _game.value = null
        settings.remove("SAVED_GAME")
    }

    override fun observeGame(): Flow<GameDto?> {
        return _game.asStateFlow()
    }
}