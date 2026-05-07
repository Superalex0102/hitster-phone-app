package com.rdisoftware.chronobeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.domain.player.AndroidSpotifyController
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    private val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    private val api by lazy {
        ChronoBeatApi(
            httpClient = httpClient
        )
    }

    private lateinit var spotifyController: AndroidSpotifyController

    private val repo by lazy {
        MusicRepositoryImpl(api, spotifyController, TokenManager)
    }

    private val CLIENT_ID = "a339f75684f44d219611845c893d1f6e"
    private val REDIRECT_URI = "chronobeat://callback"
    private val AUTH_REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        spotifyController = AndroidSpotifyController(this, TokenManager)

        setContent {
            App(repo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        spotifyController.handleAuthResponse(requestCode, resultCode, data)
    }
}