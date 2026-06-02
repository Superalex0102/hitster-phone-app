package com.rdisoftware.chronobeat.domain.usecases

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Game
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetGameUseCase(
    private val activeGameRepository: ActiveGameRepository,
    private val musicRepository: MusicRepository,
    private val getTeamsUseCase: GetTeamsUseCase
) {

    suspend operator fun invoke(): Game? = coroutineScope {
        val dto = activeGameRepository.getGame() ?: return@coroutineScope null

        val allTeams = getTeamsUseCase()

        if (allTeams.isEmpty()) return@coroutineScope null

        val neededTrackIds = mutableSetOf<String>()
        neededTrackIds.add(dto.currentTrackId)
        dto.collectedCardIdsByTeamId.values.forEach { neededTrackIds.addAll(it) }

        val trackMap = neededTrackIds.map { trackId ->
            async {
                val track = runCatching { musicRepository.getTrackInfo(trackId) }.getOrNull()
                if (track != null) trackId to track else null
            }
        }.awaitAll().filterNotNull().toMap()

        val currentTrack = trackMap[dto.currentTrackId]
            ?: Track(dto.currentTrackId, "Unknown", "Unknown", emptyList(), 0, false)

        val teams = dto.teamIds.mapNotNull { id -> allTeams.find { it.id == id } }
        val currentTeam = allTeams.first { it.id == dto.currentTeamId }
        val winnerTeam = dto.winnerTeamId?.let { id -> allTeams.find { it.id == id } }

        val collectedCards = dto.collectedCardIdsByTeamId.mapNotNull { (teamId, trackIds) ->
            val team = allTeams.find { it.id == teamId } ?: return@mapNotNull null
            val tracks = trackIds.mapNotNull { trackId -> trackMap[trackId] }
            team to tracks
        }.toMap()

        Game(
            id = dto.id,
            teams = teams,
            currentTeam = currentTeam,
            currentTrack = currentTrack,
            collectedCardsByTeam = collectedCards,
            playlistId = dto.playlistId,
            winnerTeam = winnerTeam
        )
    }
}