import Foundation
import SpotifyiOS
import Shared

class IOSSpotifyController: NSObject, SpotifyPlayerController, SPTAppRemoteDelegate {

    let clientID = AppConfig.shared.SPOTIFY_CLIENT_ID

    let redirectURI = URL(string: "chronobeat://callback")!
    
    var appRemote: SPTAppRemote?

    private let tokenKey = "spotify_access_token_cache"
    
    override init() {
        super.init()
        let configuration = SPTConfiguration(clientID: clientID, redirectURL: redirectURI)
        self.appRemote = SPTAppRemote(configuration: configuration, logLevel: .debug)
        self.appRemote?.delegate = self

        if let savedToken = UserDefaults.standard.string(forKey: tokenKey) {
            print("Found saved Spotify token, attempting to reconnect...")
            self.appRemote?.connectionParameters.accessToken = savedToken
            TokenManager.shared.accessToken = savedToken
            self.appRemote?.connect()
        }
    }

    func authenticate() {
        self.appRemote?.authorizeAndPlayURI("")
    }
    
    func playTrack(trackId: String) {
        let uri = "spotify:track:\(trackId)"
        
        if self.appRemote?.isConnected == true {
            self.appRemote?.playerAPI?.play(uri, callback: nil)
        } else {
            self.appRemote?.authorizeAndPlayURI(uri)
        }
    }

    func handleAuth(url: URL) {
        guard let parameters = self.appRemote?.authorizationParameters(from: url) else { return }

        if let token = parameters[SPTAppRemoteAccessTokenKey] as? String {
            self.appRemote?.connectionParameters.accessToken = token
            self.appRemote?.connect()

            TokenManager.shared.accessToken = token

            UserDefaults.standard.set(token, forKey: tokenKey)
            print("Token saved to UserDefaults.")

        } else if let error = parameters[SPTAppRemoteErrorDescriptionKey] as? String {
            print("Spotify error: \(error)")
        }
    }

    func resume() {
        if self.appRemote?.isConnected == true {
            self.appRemote?.playerAPI?.resume(nil)
        } else {
            self.appRemote?.connect()
        }
    }
    
    func pause() {
        self.appRemote?.playerAPI?.pause(nil)
    }
    
    func disconnect() {
        self.appRemote?.disconnect()
    }
    
    func appRemoteDidEstablishConnection(_ appRemote: SPTAppRemote) {
        print("Successfully connected to Spotify iOS!")
    }
    
    func appRemote(_ appRemote: SPTAppRemote, didDisconnectWithError error: Error?) {
        print("Disconnected.")
    }
    
    func appRemote(_ appRemote: SPTAppRemote, didFailConnectionAttemptWithError error: Error?) {
        print("Failed connection: \(String(describing: error))")
    }
}
