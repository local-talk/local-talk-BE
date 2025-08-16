package com.localtalk.api.auth.application.dto

data class SocialLoginInfo(
    val accessToken: String,
    val refreshToken: String,
    val isSignedUser: Boolean,
)
