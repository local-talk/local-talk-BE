package com.localtalk.api.auth.application.dto

data class TokenRefreshInfo(
    val accessToken: String,
    val refreshToken: String,
)