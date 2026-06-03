package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rdisoftware.chronobeat.presentation.enums.LeaderBoardMode
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardEffect
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardEvent
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LeaderBoardScreen(
    mode: LeaderBoardMode,
    onNavigateBack: () -> Unit,
) {
    val viewModel: LeaderBoardViewModel = koinViewModel(
        parameters = { parametersOf(mode) }
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LeaderBoardEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { viewModel.onEvent(LeaderBoardEvent.OnContinueOrCloseButtonClick) },
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                when (mode) {
                    LeaderBoardMode.FROM_GAME_SCREEN -> Text("Continue")
                    LeaderBoardMode.FROM_GAMESUMMARY_SCREEN -> Text("Close")
                }
            }
        }
    }
}