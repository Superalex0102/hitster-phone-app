package com.rdisoftware.chronobeat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.presentation.screens.GameScreen
import com.rdisoftware.chronobeat.presentation.screens.GameSummaryScreen
import com.rdisoftware.chronobeat.presentation.screens.HomeScreen
import com.rdisoftware.chronobeat.presentation.screens.TeamSelectionScreen
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = HomeViewModel(),
                onLocalGameClicked = {
                    navController.navigate(TeamSelectionRoute)
                }
            )
        }

        composable<TeamSelectionRoute> {
            TeamSelectionScreen(
                viewModel = TeamSelectionViewModel(),
                onTeamsSelectedClicked = {
                    navController.navigate(GameRoute)
                }
            )
        }

        composable<GameRoute> {
            GameScreen(
                viewModel = GameViewModel(),
                onGameFinishedClicked = {
                    navController.navigate(GameSummaryRoute)
                }
            )
        }

        composable<GameSummaryRoute> {
            GameSummaryScreen(
                onHomeClicked = {
                    navController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPlayAgainClicked = {
                    navController.navigate(TeamSelectionRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}