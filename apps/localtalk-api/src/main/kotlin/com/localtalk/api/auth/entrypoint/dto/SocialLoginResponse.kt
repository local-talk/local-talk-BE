package com.localtalk.api.auth.entrypoint.dto

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isSignedUser: Boolean,
)
