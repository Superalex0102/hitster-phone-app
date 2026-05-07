package com.rdisoftware.chronobeat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.presentation.screens.HomeScreen
import com.rdisoftware.chronobeat.presentation.screens.SpotifyPlayerScreen

@Composable
fun App(musicRepository: MusicRepository) {
    MaterialTheme {
        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                //HomeScreen()
                SpotifyPlayerScreen(musicRepository = musicRepository)
            }
        }
    }
}