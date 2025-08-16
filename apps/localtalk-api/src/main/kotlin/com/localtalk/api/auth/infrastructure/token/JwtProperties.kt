package com.localtalk.api.auth.infrastructure.token

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties(
    val secret: String = "local-talk-secret-key-for-development-only-please-change-in-production",
    val accessTokenExpiry: Long = 3600,
    val refreshTokenExpiry: Long = 604800,
)
