package com.rdisoftware.chronobeat.data.repositories

import com.rdisoftware.chronobeat.domain.repositories.TokenRepository

class TokenRepositoryImpl : TokenRepository {
    override suspend fun getAccessToken(): String {
        // For testing, return your temporary Spotify Console token
        return "BQC..."
    }
}