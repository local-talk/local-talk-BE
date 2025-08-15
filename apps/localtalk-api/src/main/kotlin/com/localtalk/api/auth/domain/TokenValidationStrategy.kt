package com.localtalk.api.auth.domain

interface TokenValidationStrategy {

    val supportedProviderName: String

    suspend fun validate(accessToken: String): TokenValidationInfo
}
