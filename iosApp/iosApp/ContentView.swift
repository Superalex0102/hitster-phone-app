import SwiftUI
import Shared

struct ContentView: View {
    let spotifyController = IOSSpotifyController()
    let musicRepository: MusicRepository
    
    init() {
        self.musicRepository = Koin_iosKt.getMusicRepository()
    }
    
    var body: some View {
        SpotifyPlayerView(musicRepository: musicRepository)
    }
}
