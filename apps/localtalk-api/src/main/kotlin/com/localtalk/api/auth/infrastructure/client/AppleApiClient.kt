package com.localtalk.api.auth.infrastructure.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AppleApiClient(
    val webClient: WebClient
) {

    suspend fun validateToken(identityToken: String): AppleTokenValidationResult {
        TODO()
    }

}

data class AppleTokenValidationResult(
    val isValid: Boolean,
    val sub: String?,
    val email: String?,
    val error: String? = null
)
