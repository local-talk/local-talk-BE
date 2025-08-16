package com.localtalk.api.auth.infrastructure.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

class JwtTokenGenerator(
    val secretKey: String
) {

    val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    fun createToken(
        userId: Long,
        role: String,
        issuedAt: Instant,
        expiresAt: Instant
    ): String = Jwts.builder()
        .claim("userId", userId)
        .claim("role", role)
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiresAt))
        .signWith(key)
        .compact()
}
