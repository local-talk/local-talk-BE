package com.localtalk.api.auth.domain

interface ExternalTokenValidator {
    suspend fun validateToken(accessToken: String, providerName: String): TokenValidationInfo
}
