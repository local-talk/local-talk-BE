package com.localtalk.api.auth.infrastructure

import com.localtalk.api.auth.infrastructure.token.JwtTokenGenerator
import com.localtalk.api.utils.JwtTestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class JwtTokenGeneratorTest {

    val secretKey = "test-secret-key-for-jwt-token-generation-must-be-long-enough-for-hmac-sha256"
    val jwtTokenGenerator = JwtTokenGenerator(secretKey)

    @Nested
    inner class `JWT 토큰을 생성할 때` {

        @Test
        fun `유효한 JWT 구조의 토큰을 생성한다`() {
            val userId = 123L
            val role = "MEMBER"
            val issuedAt = Instant.now()
            val expiresAt = issuedAt.plusSeconds(3600)

            val token = jwtTokenGenerator.createToken(userId, role, issuedAt, expiresAt)

            assertThat(JwtTestUtils.isValidJwtFormat(token)).isTrue()
        }

    }
}
