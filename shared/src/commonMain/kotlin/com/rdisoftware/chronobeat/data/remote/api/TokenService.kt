package com.rdisoftware.chronobeat.data.remote.api

class TokenService {
    suspend fun getAccessToken(): String {
        // For testing, return your temporary Spotify Console token
        return "BQC..."
    }
}