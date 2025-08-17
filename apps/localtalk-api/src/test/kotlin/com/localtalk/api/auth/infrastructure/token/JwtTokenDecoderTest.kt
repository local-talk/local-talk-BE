package com.localtalk.api.auth.infrastructure.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

class JwtTokenDecoderTest {

    val jwtProperties = JwtProperties(
        secret = SECRET_KEY,
        accessTokenExpiry = 3600,
        refreshTokenExpiry = 86400,
    )
    val jwtTokenDecoder = JwtTokenDecoder(jwtProperties)
    val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))

    @Nested
    inner class `JWT 토큰을 디코딩할 때` {

        @Test
        fun `날짜 클레임이 포함된 토큰을 올바르게 디코딩한다`() {
            val issuedAt = Instant.ofEpochSecond(Instant.now().epochSecond)
            val expiresAt = issuedAt.plusSeconds(3600)

            val token = Jwts.builder()
                .claim(ID_CLAIM, 123L)
                .claim(ROLE_CLAIM, "MEMBER")
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact()

            val result = jwtTokenDecoder.decodeToJwt(token)

            assertThat(result.tokenValue).isEqualTo(token)
            assertThat(result.getClaim<Long>(ID_CLAIM)).isEqualTo(123L)
            assertThat(result.getClaim<String>(ROLE_CLAIM)).isEqualTo("MEMBER")
            assertThat(result.issuedAt).isEqualTo(issuedAt)
            assertThat(result.expiresAt).isEqualTo(expiresAt)
        }

        @Test
        fun `일반 클레임만 있는 토큰을 올바르게 디코딩한다`() {
            val token = Jwts.builder()
                .claim(ID_CLAIM, 456L)
                .claim(ROLE_CLAIM, "TEMPORARY")
                .claim("customClaim", "customValue")
                .signWith(key)
                .compact()

            val result = jwtTokenDecoder.decodeToJwt(token)

            assertThat(result.tokenValue).isEqualTo(token)
            assertThat(result.getClaim<Long>(ID_CLAIM)).isEqualTo(456L)
            assertThat(result.getClaim<String>(ROLE_CLAIM)).isEqualTo("TEMPORARY")
            assertThat(result.getClaim<String>("customClaim")).isEqualTo("customValue")
        }

        @Test
        fun `헤더 정보가 올바르게 설정된다`() {
            val token = Jwts.builder()
                .claim(ID_CLAIM, 123L)
                .signWith(key)
                .compact()

            val result = jwtTokenDecoder.decodeToJwt(token)

            assertThat(result.headers).containsEntry("typ", "JWT")
            assertThat(result.headers).containsEntry("alg", "HS256")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "invalid.jwt.token"])
        fun `잘못된 형식의 토큰이면 예외를 발생시킨다`(invalidToken: String) {
            assertThatThrownBy { jwtTokenDecoder.decodeToJwt(invalidToken) }
                .isInstanceOf(Exception::class.java)
        }

        @Test
        fun `다른 키로 서명된 토큰이면 예외를 발생시킨다`() {
            val wrongKey = Keys.hmacShaKeyFor("wrong-secret-key-different-from-original-key-for-testing".toByteArray(StandardCharsets.UTF_8))
            val token = Jwts.builder()
                .claim(ID_CLAIM, 123L)
                .signWith(wrongKey)
                .compact()

            assertThatThrownBy { jwtTokenDecoder.decodeToJwt(token) }
                .isInstanceOf(Exception::class.java)
        }
    }

    companion object {
        const val SECRET_KEY = "test-secret-key-for-jwt-token-generation-must-be-long-enough-for-hmac-sha256"
    }
}
