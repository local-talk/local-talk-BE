package com.localtalk.api.auth.infrastructure.token

import com.localtalk.logging.common.logger
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

const val ID_CLAIM = "id"
const val ROLE_CLAIM = "role"

class JwtTokenHandler(
    secretKey: String,
) {
    private val log = logger()

    val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    fun createToken(
        id: Long,
        role: String,
        issuedAt: Instant,
        expiresAt: Instant,
    ): String = Jwts.builder()
        .claim(ID_CLAIM, id)
        .claim(ROLE_CLAIM, role)
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiresAt))
        .signWith(key)
        .compact()

    fun parseToken(token: String): Claims = try {
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    } catch (e: ExpiredJwtException) {
        log.debug("Expired JWT token: {}", token, e)
        throw IllegalStateException("만료된 토큰입니다.")
    }
}
