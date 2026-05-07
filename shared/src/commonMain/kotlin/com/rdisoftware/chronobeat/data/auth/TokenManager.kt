package com.rdisoftware.chronobeat.data.auth

import com.rdisoftware.chronobeat.shared.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TokenManager {
    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()
    var accessToken: String?
        get() = _accessTokenFlow.value ?: BuildConfig.SPOTIFY_ACCESS_TOKEN_DEBUG
        set(value) {
            _accessTokenFlow.value = value
        }
}