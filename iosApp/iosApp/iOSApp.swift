import SwiftUI
import Shared

@main
struct iOSApp: App {
    let spotifyController = IOSSpotifyController()

    init() {
        Koin_iosKt.startKoinIOS(spotifyController: spotifyController)
    }
    
    var body: some Scene {
        WindowGroup {
//            ContentView()
//                .onOpenURL { url in
//                    spotifyController.handleAuth(url: url)
//                }
            GameScreen()
        }
    }
}
