package com.localtalk.api.auth.domain

data class TokenValidationInfo(
    val isValid: Boolean,
    val socialKey: String,
    val nickname: String,
)
