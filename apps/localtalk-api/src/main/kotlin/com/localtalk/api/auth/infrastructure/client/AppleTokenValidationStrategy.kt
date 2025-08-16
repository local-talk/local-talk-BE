package com.localtalk.api.auth.infrastructure.client

import com.localtalk.api.auth.domain.TokenValidationInfo
import com.localtalk.api.auth.domain.TokenValidationStrategy
import org.springframework.stereotype.Component

@Component
class AppleTokenValidationStrategy(
    val appleApiClient: AppleApiClient,
) : TokenValidationStrategy {

    override val supportedProviderName: String = "APPLE"

    override suspend fun validate(accessToken: String): TokenValidationInfo {
        TODO()
    }
}
