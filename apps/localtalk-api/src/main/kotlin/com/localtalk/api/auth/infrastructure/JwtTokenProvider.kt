package com.localtalk.api.auth.infrastructure

import com.localtalk.api.auth.domain.Role
import com.localtalk.api.auth.domain.TokenProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.temporal.ChronoUnit

@Component
@EnableConfigurationProperties(JwtProperties::class)
class JwtTokenProvider(
    val jwtProperties: JwtProperties,
    val clock: Clock = Clock.systemDefaultZone(),
) : TokenProvider {

    val tokenGenerator = JwtTokenGenerator(jwtProperties.secret)

    override fun generateToken(userId: Long, role: Role): Pair<String, String> {
        val now = clock.instant()
        
        val accessToken = tokenGenerator.createToken(
            userId = userId,
            role = role.name,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.accessTokenExpiry, ChronoUnit.SECONDS)
        )
        
        val refreshToken = tokenGenerator.createToken(
            userId = userId,
            role = role.name,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.refreshTokenExpiry, ChronoUnit.SECONDS)
        )
        
        return accessToken to refreshToken
    }
}
