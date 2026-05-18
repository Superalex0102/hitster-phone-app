import Foundation
import SpotifyiOS
import Shared
import os

class IOSSpotifyController: NSObject, SpotifyPlayerController, SPTAppRemoteDelegate {

    let clientID = AppConfig.shared.SPOTIFY_CLIENT_ID
    let redirectURI = URL(string: "chronobeat://callback")!

    var appRemote: SPTAppRemote?

    private let tokenKey = "spotify_access_token_cache"

    private var pendingAction: (() -> Void)?

    private let logger = Logger(subsystem: Bundle.main.bundleIdentifier ?? "com.rdisoftware.chronobeat", category: "IOSSpotifyController")

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

    private func connectAndExecute(action: @escaping () -> Void) {
        if self.appRemote?.isConnected == true {
            action()
        } else {
            self.pendingAction = action
            self.appRemote?.connect()
        }
    }

    func playTrack(trackId: String) {
        let uri = "spotify:track:\(trackId)"
        connectAndExecute {
            self.appRemote?.playerAPI?.play(uri, callback: nil)
        }
    }

    func resume() {
        connectAndExecute {
            self.appRemote?.playerAPI?.resume(nil)
        }
    }

    func pause() {
        self.appRemote?.playerAPI?.pause(nil)
    }

    func disconnect() {
        self.appRemote?.disconnect()
    }

    func handleAuth(url: URL) {
        guard let parameters = self.appRemote?.authorizationParameters(from: url) else { return }

        if let token = parameters[SPTAppRemoteAccessTokenKey] as? String {
            self.appRemote?.connectionParameters.accessToken = token
            TokenManager.shared.accessToken = token
            self.appRemote?.connect()

            UserDefaults.standard.set(token, forKey: tokenKey)
            print("Token saved to UserDefaults.")

        } else if let error = parameters[SPTAppRemoteErrorDescriptionKey] as? String {
            logger.error("Spotify authentication error: \(error, privacy: .public)")
            self.pendingAction = nil
        }
    }

    func appRemoteDidEstablishConnection(_ appRemote: SPTAppRemote) {
        logger.info("Successfully connected to Spotify.")

        if let action = pendingAction {
            logger.debug("Executing pending Spotify action after connection.")
            action()
            pendingAction = nil
        }
    }

    func appRemote(_ appRemote: SPTAppRemote, didDisconnectWithError error: Error?) {
        if let error = error {
            logger.warning("Connection lost: \(error.localizedDescription, privacy: .public)")
        } else {
            logger.info("Disconnected")
        }
    }

    func appRemote(_ appRemote: SPTAppRemote, didFailConnectionAttemptWithError error: Error?) {
        logger.error("Error while connecting: \(String(describing: error), privacy: .public)")
        logger.info("iOS Auth process (Apple native) starting in the background...")
        self.authenticate()
    }
}