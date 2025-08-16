package com.localtalk.api.auth.domain

data class TokenValidationInfo(
    val provider: String,
    val socialKey: String,
)
