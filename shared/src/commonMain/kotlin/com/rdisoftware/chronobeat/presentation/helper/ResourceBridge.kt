package com.rdisoftware.chronobeat.presentation.helper

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

object ResourceBridge {

    @Throws(Exception::class)
    suspend fun getStringAsync(resource: StringResource): String {
        return getString(resource)
    }
}