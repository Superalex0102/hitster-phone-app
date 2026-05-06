package com.rdisoftware.chronobeat.navigation

import TeamSelectionScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.ui.screens.GameScreen
import com.rdisoftware.chronobeat.ui.screens.GameSummaryScreen
import com.rdisoftware.chronobeat.ui.screens.HomeScreen

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
                onTeamsSelectedClicked = {
                    navController.navigate(GameRoute)
                }
            )
        }

        composable<GameRoute> {
            GameScreen(
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