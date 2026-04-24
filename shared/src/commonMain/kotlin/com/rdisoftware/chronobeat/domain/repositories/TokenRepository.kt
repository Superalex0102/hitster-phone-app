package com.rdisoftware.chronobeat.domain.repositories

interface TokenRepository {
    suspend fun getAccessToken(): String
}