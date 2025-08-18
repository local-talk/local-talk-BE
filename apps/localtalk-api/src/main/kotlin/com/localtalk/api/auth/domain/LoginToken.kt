package com.localtalk.api.auth.domain

data class LoginToken(
    val accessToken: String,
    val refreshToken: String,
)
