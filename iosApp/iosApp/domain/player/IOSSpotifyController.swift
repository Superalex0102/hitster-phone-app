import Foundation
import SpotifyiOS
import Shared

class IOSSpotifyController: NSObject, SpotifyPlayerController, SPTAppRemoteDelegate {
    
    let clientID = "a339f75684f44d219611845c893d1f6e"
    let redirectURI = URL(string: "chronobeat://callback")!
    
    var appRemote: SPTAppRemote?
    
    override init() {
        super.init()
        let configuration = SPTConfiguration(clientID: clientID, redirectURL: redirectURI)
        self.appRemote = SPTAppRemote(configuration: configuration, logLevel: .debug)
        self.appRemote?.delegate = self
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
