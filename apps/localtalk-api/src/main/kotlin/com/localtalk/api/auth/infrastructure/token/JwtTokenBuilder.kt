package com.localtalk.api.auth.infrastructure.token

import com.localtalk.api.auth.domain.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class JwtTokenBuilder(
    val jwtProperties: JwtProperties,
    val clock: Clock
) {

    val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray(StandardCharsets.UTF_8))

    fun generateAccessToken(userId: Long, role: Role): String {
        val now = clock.instant()
        return buildToken(
            userId = userId,
            role = role,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.accessTokenExpiry, ChronoUnit.SECONDS)
        )
    }

    fun generateRefreshToken(userId: Long, role: Role): String {
        val now = clock.instant()
        return buildToken(
            userId = userId,
            role = role,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.refreshTokenExpiry, ChronoUnit.SECONDS)
        )
    }

    fun buildToken(
        userId: Long,
        role: Role,
        issuedAt: Instant,
        expiresAt: Instant
    ): String = Jwts.builder()
        .claim("userId", userId)
        .claim("role", role.name)
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiresAt))
        .signWith(key)
        .compact()
}
