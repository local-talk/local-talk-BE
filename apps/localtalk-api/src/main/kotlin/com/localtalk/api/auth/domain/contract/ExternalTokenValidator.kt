package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.SocialLoginIdentifier

interface ExternalTokenValidator {
    suspend fun validateToken(accessToken: String, providerName: String): SocialLoginIdentifier
}
