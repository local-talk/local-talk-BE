package com.localtalk.api.auth.infrastructure

import com.localtalk.api.auth.domain.Role
import com.localtalk.api.auth.infrastructure.token.JwtProperties
import com.localtalk.api.auth.infrastructure.token.JwtTokenProvider
import com.localtalk.api.utils.JwtTestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class JwtTokenProviderTest {

    val fixedTime: Instant = Instant.parse("2024-01-01T00:00:00Z")
    val fixedClock: Clock = Clock.fixed(fixedTime, ZoneOffset.UTC)
    val jwtProperties = JwtProperties(
        secret = "test-secret-key-for-jwt-token-generation-must-be-long-enough-for-hmac-sha256",
        accessTokenExpiry = 3600L,
        refreshTokenExpiry = 604800L,
    )
    val jwtTokenProvider = JwtTokenProvider(jwtProperties, fixedClock)

    @Nested
    inner class `토큰을 생성할 때` {

        @Test
        fun `Access Token과 Refresh Token 쌍을 반환한다`() {
            val userId = 123L
            val role = Role.MEMBER

            val (accessToken, refreshToken) = jwtTokenProvider.generateToken(userId, role)

            assertThat(JwtTestUtils.isValidJwtFormat(accessToken)).isTrue
            assertThat(JwtTestUtils.isValidJwtFormat(refreshToken)).isTrue
        }
    }
}
