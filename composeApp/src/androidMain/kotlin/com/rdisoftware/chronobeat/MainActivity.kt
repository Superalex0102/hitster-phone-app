package com.rdisoftware.chronobeat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.domain.player.AndroidSpotifyController
import com.rdisoftware.chronobeat.domain.player.SpotifyPlayerController
import com.rdisoftware.chronobeat.presentation.screens.SpotifyPlayerScreen
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    lateinit var spotifyController: AndroidSpotifyController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        spotifyController = AndroidSpotifyController(this, TokenManager)

        loadKoinModules(module {
            single<SpotifyPlayerController> { spotifyController }
        })

        setContent {
            // TODO (Architecture - Spotify & MusicRepository):
            // In the future, when screens need the music player, DO NOT pass it as a parameter to App()!
            // To keep the UI layer clean and avoid prop drilling, we will use Dependency Injection (e.g., Koin).
            // The DI module will call 'RepositoryFactory.createMusicRepository(spotifyController)'
            // and inject the ready-to-use repository directly into the target Screen's ViewModel.
            App()
            //SpotifyPlayerScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        spotifyController.handleAuthResponse(requestCode, resultCode, data)
    }
}