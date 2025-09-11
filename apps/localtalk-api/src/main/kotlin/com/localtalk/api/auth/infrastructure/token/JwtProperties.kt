package com.localtalk.api.auth.infrastructure.token

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "local-talk-secret-key-for-development-only-please-change-in-production",
    var accessTokenExpiry: Long = 3600,
    var refreshTokenExpiry: Long = 604800,
)
