package com.rdisoftware.chronobeat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rdisoftware.chronobeat.data.auth.TokenManager
import com.rdisoftware.chronobeat.domain.player.AndroidSpotifyController

class MainActivity : ComponentActivity() {
    lateinit var spotifyController: AndroidSpotifyController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        spotifyController = AndroidSpotifyController(this, TokenManager)

        setContent {
            // TODO (Architektúra - Spotify & MusicRepository):
            // Később, amikor a képernyőknek szüksége lesz a zenelejátszóra, NE az App()-nak adjuk át paraméterként!
            // A tiszta UI (Prop Drilling elkerülése) érdekében Dependency Injection-t (pl. Koin) fogunk használni.
            // A DI modul fogja meghívni a 'RepositoryFactory.createMusicRepository(spotifyController)'-t,
            // és a kész repót közvetlenül az adott Screen ViewModel-jébe fogja injektálni.
            App()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        spotifyController.handleAuthResponse(requestCode, resultCode, data)
    }
}