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
            val onLocalGame = {
                navController.navigate(TeamSelectionRoute)
            }

            HomeScreen(
                viewModel = HomeViewModel(onLocalGame)
            )
        }

        composable<TeamSelectionRoute> {
            //TODO: Implement navigation according to the HomeScreen solution - dependency on TeamSelectionViewModel
            TeamSelectionScreen(
                onTeamsSelected = {
                    navController.navigate(GameRoute)
                }
            )
        }

        composable<GameRoute> {
            //TODO: Implement navigation according to the HomeScreen solution - dependency on GameViewModel
            GameScreen(
                onGameFinished = {
                    navController.navigate(GameSummaryRoute)
                }
            )
        }

        composable<GameSummaryRoute> {
            //TODO: Implement navigation according to the HomeScreen solution - dependency on GameSummaryViewModel
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