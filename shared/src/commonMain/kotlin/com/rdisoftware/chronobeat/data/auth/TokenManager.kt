package com.rdisoftware.chronobeat.data.auth

import com.rdisoftware.chronobeat.shared.BuildConfig

class TokenManager {
    suspend fun getAccessToken(): String {
        return BuildConfig.SPOTIFY_ACCESS_TOKEN_DEBUG
    }
}