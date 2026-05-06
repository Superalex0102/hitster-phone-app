package com.rdisoftware.chronobeat.navigation

import TeamSelectionScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onLocalGame = {
                    navController.navigate(TeamSelectionRoute)
                }
            )
        }

        composable<TeamSelectionRoute> {
            TeamSelectionScreen(
                onTeamsSelected = {
                    navController.navigate(GameRoute)
                }
            )
        }

        composable<GameRoute> {
            GameScreen(
                onGameFinished = {
                    navController.navigate(GameSummaryRoute)
                }
            )
        }

        composable<GameSummaryRoute> {
            GameSummaryScreen(
                onBackToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPlayAgain = {
                    navController.navigate(TeamSelectionRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}