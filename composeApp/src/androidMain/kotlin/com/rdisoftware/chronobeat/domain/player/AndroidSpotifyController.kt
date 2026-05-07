package com.rdisoftware.chronobeat.domain.player

import android.app.Activity
import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.shared.BuildConfig
class AndroidSpotifyController(
    private val activity: Activity,
    private val tokenManager: TokenManager
) : SpotifyPlayerController {

    private val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    private val redirectUri = "chronobeat://callback"
    private val AUTH_REQUEST_CODE = 1337

    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun authenticate() {
        val builder = AuthorizationRequest.Builder(
            clientId,
            AuthorizationResponse.Type.TOKEN,
            redirectUri
        )
        builder.setScopes(arrayOf("streaming", "user-read-private", "playlist-read-private"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(activity, AUTH_REQUEST_CODE, request)
    }

    fun handleAuthResponse(requestCode: Int, resultCode: Int, intent: android.content.Intent?) {
        if (requestCode == AUTH_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("SpotifySDK", "Successfully authenticated, access token: ${response.accessToken}")
                    tokenManager.accessToken = response.accessToken
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("SpotifySDK", "Auth error: ${response.error}")
                }
                else -> {
                    Log.w("SpotifySDK", "Authentication cancelled: ${response.type}")
                }
            }
        }
    }

    private fun connectAndExecute(action: () -> Unit) {
        if (spotifyAppRemote?.isConnected == true) {
            action()
            return
        }

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(false)
            .build()

        SpotifyAppRemote.connect(activity, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                Log.d("SpotifySDK", "Successfully connected to Spotify App Remote")
                spotifyAppRemote = appRemote
                action()
            }
            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifySDK", "Error while connecting: ", throwable)
            }
        })
    }

    override fun playTrack(trackId: String) {
        connectAndExecute {
            val uri = "spotify:track:$trackId"
            spotifyAppRemote?.playerApi?.play(uri)
        }
    }

    override fun resume() {
        connectAndExecute {
            spotifyAppRemote?.playerApi?.resume()
        }
    }

    override fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    override fun disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        spotifyAppRemote = null
    }
}