package com.localtalk.api.auth.infrastructure.token

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class JwtTokenHandlerTest {

    val secretKey = "test-secret-key-for-jwt-token-generation-must-be-long-enough-for-hmac-sha256"
    val jwtTokenHandler = JwtTokenHandler(secretKey)

    @Nested
    inner class `토큰을 생성할 때` {

        @Test
        fun `유효한 토큰이 생성된다`() {
            val id = 123L
            val role = "MEMBER"
            val now = Instant.now()
            val expiresAt = now.plusSeconds(3600)

            val token = jwtTokenHandler.createToken(id, role, now, expiresAt)

            assertThat(token).isNotBlank()
            assertThat(token.split(".")).hasSize(3)
        }
    }

    @Nested
    inner class `유효한 토큰을 파싱할 때` {

        @Test
        fun `Claims가 정상적으로 반환된다`() {
            val id = 123L
            val role = "MEMBER"
            val now = Instant.now()
            val expiresAt = now.plusSeconds(3600)
            val token = jwtTokenHandler.createToken(id, role, now, expiresAt)

            val claims = jwtTokenHandler.parseToken(token)

            assertThat(claims.get(ID_CLAIM, Number::class.java).toLong()).isEqualTo(id)
            assertThat(claims.get(ROLE_CLAIM, String::class.java)).isEqualTo(role)
        }
    }

    @Nested
    inner class `만료된 토큰을 파싱할 때` {

        @Test
        fun `IllegalStateException을 발생시킨다`() {
            val id = 123L
            val role = "MEMBER"
            val now = Instant.now()
            val pastTime = now.minusSeconds(3600) // 1시간 전 만료
            val expiredToken = jwtTokenHandler.createToken(id, role, pastTime.minusSeconds(3600), pastTime)

            assertThatThrownBy { jwtTokenHandler.parseToken(expiredToken) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("만료된 토큰입니다.")
        }
    }
}