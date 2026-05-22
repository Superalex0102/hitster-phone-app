import com.rdisoftware.chronobeat.domain.models.Playlist
import com.rdisoftware.chronobeat.domain.models.PlaylistSummary
import com.rdisoftware.chronobeat.domain.models.Track
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository

class FakeMusicRepository : MusicRepository {
    var lastPlayedTrackId: String? = null
    var isPausedCalled = false
    var isResumedCalled = false

    override suspend fun getUserPlaylistsSummary(): List<PlaylistSummary> = emptyList()

    override suspend fun getUserPlaylistDetails(playlistId: String): Playlist =
        Playlist("dummy_id", "Dummy Playlist", emptyList())

    override suspend fun getChronobeatPlaylists(): List<Playlist> = emptyList()

    override suspend fun getTrackInfo(trackId: String): Track =
        Track(
            "dummy_id", "Dummy Title", "Dummy Artist", emptyList(), 2000,
            isPlayable = true
        )

    override suspend fun playMusic(trackId: String) {
        lastPlayedTrackId = trackId
    }

    override suspend fun resumeMusic() {
        isResumedCalled = true
    }

    override suspend fun pauseMusic() {
        isPausedCalled = true
    }
}