package com.rdisoftware.chronobeat.data.remote.api

import com.rdisoftware.chronobeat.shared.BuildConfig

class TokenService {
    suspend fun getAccessToken(): String {
        // For testing, return your temporary Spotify Console token
        return BuildConfig.SPOTIFY_ACCESS_TOKEN_DEBUG
    }
}