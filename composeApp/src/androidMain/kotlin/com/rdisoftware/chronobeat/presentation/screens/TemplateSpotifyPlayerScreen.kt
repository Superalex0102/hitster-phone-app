package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SpotifyPlayerScreen(
    musicRepository: MusicRepository = koinInject()
) {
    val coroutineScope = rememberCoroutineScope()

    var isPlaying by remember { mutableStateOf(false) }

    var hasStarted by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Spotify KMP Teszt",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (isPlaying) {
                            musicRepository.pauseMusic()
                        } else {
                            if (!hasStarted) {
                                musicRepository.playMusic("4PTG3Z6ehGkBFwjybzWkR8")
                                hasStarted = true
                            } else {
                                musicRepository.resumeMusic()
                            }
                        }
                        isPlaying = !isPlaying
                    }
                },
                modifier = Modifier
                    .height(56.dp)
                    .width(220.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isPlaying) "⏸ Szünet" else "▶ Zene Elindítása",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}