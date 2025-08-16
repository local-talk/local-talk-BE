package com.localtalk.api.auth.infrastructure.token

import com.localtalk.api.auth.domain.SocialLoginIdentifier

interface TokenValidationStrategy {

    val supportedProviderName: String

    suspend fun validate(accessToken: String): SocialLoginIdentifier
}
