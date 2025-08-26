package com.localtalk.api.auth.infrastructure.token

import com.localtalk.api.auth.domain.AuthMember
import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.contract.TokenProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.temporal.ChronoUnit

@Component
@EnableConfigurationProperties(JwtProperties::class)
class JwtTokenProvider(
    val jwtProperties: JwtProperties,
    val clock: Clock,
) : TokenProvider {

    val tokenHandler = JwtTokenHandler(jwtProperties.secret)

    override fun generateToken(id: Long, role: String): Pair<String, String> {
        val now = clock.instant()

        val accessToken = tokenHandler.createToken(
            id = id,
            role = role,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.accessTokenExpiry, ChronoUnit.SECONDS),
        )

        val refreshToken = tokenHandler.createToken(
            id = id,
            role = role,
            issuedAt = now,
            expiresAt = now.plus(jwtProperties.refreshTokenExpiry, ChronoUnit.SECONDS),
        )

        return accessToken to refreshToken
    }


    override fun parseToken(token: String): AuthMember {
        val claims = tokenHandler.parseToken(token)
        val id = (claims[ID_CLAIM] as Number).toLong()
        val role = claims.get(ROLE_CLAIM, String::class.java)

        return AuthMember(id, AuthRole.valueOf(role))
    }
}
