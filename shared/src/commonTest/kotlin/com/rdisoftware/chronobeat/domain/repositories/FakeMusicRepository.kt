import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class FakeMusicRepository : MusicRepository {

    var lastPlayedTrackId: String? = null

    private val fakeTrackIds = (1..50).map { "fake_track_$it" }

    override suspend fun getChronobeatPlaylists(): List<Playlist> {
        return listOf(
            Playlist(
                id = "test_playlist_id",
                name = "Test ChronoBeat Playlist",
                trackIds = fakeTrackIds
            )
        )
    }

    override suspend fun getTrackInfo(trackId: String): Track {
        val numberId = trackId.removePrefix("fake_track_").toIntOrNull() ?: 1

        return Track(
            id = trackId,
            title = "Test Song $numberId",
            mainArtist = "Test Artist",
            featArtists = emptyList(),
            releaseYear = 2000 + (numberId % 20),
            isPlayable = true
        )
    }


    override suspend fun getUserPlaylistsSummary(): List<PlaylistSummary> {
        return emptyList()
    }

    override suspend fun getUserPlaylistDetails(playlistId: String): Playlist {
        return Playlist(playlistId, "Test Playlist", fakeTrackIds)
    }

    override suspend fun playMusic(trackId: String) {
        lastPlayedTrackId = trackId
    }

    override suspend fun resumeMusic() {

    }

    override suspend fun pauseMusic() {

    }

    override fun hasValidToken(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate() {
        TODO("Not yet implemented")
    }
}