package com.rdisoftware.chronobeat.presentation.navigation

import com.rdisoftware.chronobeat.presentation.enums.LeaderBoardMode
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
object TeamSelectionRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
object GameRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
object GameSummaryRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
data class LeaderBoardRoute(
    val mode: LeaderBoardMode
)