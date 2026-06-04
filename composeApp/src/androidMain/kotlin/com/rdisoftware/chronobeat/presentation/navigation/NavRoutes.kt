package com.rdisoftware.chronobeat.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
object TeamSelectionRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
data class GameRoute(val shouldLoadSave: Boolean = true) //TODO: Might need data class instead of object to pass parameters

@Serializable
object GameSummaryRoute //TODO: Might need data class instead of object to pass parameters

@Serializable
object SettingsRoute //TODO: Might need data class instead of object to pass parameters