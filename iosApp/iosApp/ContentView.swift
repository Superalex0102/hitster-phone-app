import SwiftUI
import Shared

struct ContentView: View {
    let spotifyController = IOSSpotifyController()
    let musicRepository: MusicRepositoryImpl
    
    init() {
        self.musicRepository = RepositoryFactory.shared.createMusicRepository(spotifyPlayer: spotifyController)
    }
    
    var body: some View {
        SpotifyPlayerView(musicRepository: musicRepository)
            .onOpenURL { url in
                spotifyController.handleAuth(url: url)
            }
    }
}
