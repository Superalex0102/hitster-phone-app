package com.rdisoftware.chronobeat.data.auth

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TokenManagerTest {

    @BeforeTest
    @AfterTest
    fun resetState() {
        TokenManager.accessToken = null
    }

    @Test
    fun `initial token state is null`() {
        assertNull(TokenManager.accessToken, "The default access token should be null before any login occurs")
    }

    @Test
    fun `setting access token stores the value correctly`() {
        val expectedToken = "BQD_test_spotify_token_12345"

        TokenManager.accessToken = expectedToken

        assertEquals(expectedToken, TokenManager.accessToken, "A TokenManager didn't return the expected token after setting it")
    }

    @Test
    fun `clearing access token sets it back to null`() {
        TokenManager.accessToken = "dummy_token"

        TokenManager.accessToken = null

        assertNull(TokenManager.accessToken, "The access token should be null after being cleared")
    }
}