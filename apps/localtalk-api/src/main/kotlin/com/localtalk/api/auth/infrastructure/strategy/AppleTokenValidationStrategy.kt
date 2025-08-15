package com.localtalk.api.auth.infrastructure.strategy

import com.localtalk.api.auth.domain.TokenValidationInfo
import com.localtalk.api.auth.domain.TokenValidationStrategy
import com.localtalk.api.auth.infrastructure.client.AppleApiClient
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
