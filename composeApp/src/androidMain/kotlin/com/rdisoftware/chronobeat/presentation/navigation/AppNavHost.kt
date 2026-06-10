package com.rdisoftware.chronobeat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rdisoftware.chronobeat.presentation.enums.LeaderBoardMode
import com.rdisoftware.chronobeat.presentation.screens.GameScreen
import com.rdisoftware.chronobeat.presentation.screens.GameSummaryScreen
import com.rdisoftware.chronobeat.presentation.screens.HomeScreen
import com.rdisoftware.chronobeat.presentation.screens.SettingsScreen
import com.rdisoftware.chronobeat.presentation.screens.TeamSelectionScreen
import com.rdisoftware.chronobeat.presentation.screens.LeaderBoardScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onLocalGameClicked = { shouldLoadSave ->
                    if (shouldLoadSave) {
                        navController.navigate(GameRoute)
                    } else {
                        navController.navigate(TeamSelectionRoute)
                    }
                },
                onSettingsClicked = { navController.navigate(SettingsRoute) }
            )
        }

        composable<TeamSelectionRoute> {
            TeamSelectionScreen(
                onTeamsSelectedClicked = {
                    navController.navigate(GameRoute)
                }
            )
        }

        composable<GameRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<GameRoute>()

            GameScreen(
                onGameFinishedClicked = {
                    navController.navigate(GameSummaryRoute)
                },
                onLeaderBoardClicked = {
                    navController.navigate(LeaderBoardRoute(mode = LeaderBoardMode.FROM_GAME_SCREEN))
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
                },
                onShowLeaderBoardClicked = {
                    navController.navigate(LeaderBoardRoute(mode = LeaderBoardMode.FROM_GAMESUMMARY_SCREEN))
                }
            )
        }

        composable<LeaderBoardRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LeaderBoardRoute>()

            LeaderBoardScreen(
                mode = route.mode,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onSaveClicked = {
                    navController.navigate(HomeRoute)
                })
        }
    }
}
