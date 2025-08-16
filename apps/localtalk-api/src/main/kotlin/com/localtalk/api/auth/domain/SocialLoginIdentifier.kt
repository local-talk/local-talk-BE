package com.localtalk.api.auth.domain

data class SocialLoginIdentifier(
    val provider: String,
    val socialKey: String,
)
