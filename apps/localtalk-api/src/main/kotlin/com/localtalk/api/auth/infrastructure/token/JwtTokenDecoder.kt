package com.localtalk.api.auth.infrastructure.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.time.Instant


private const val JWT_TYPE = "JWT"
private const val HEADER_TYPE = "typ"
private const val HEADER_ALGORITHM = "alg"

@Component
@EnableConfigurationProperties(JwtProperties::class)
class JwtTokenDecoder(
    jwtProperties: JwtProperties,
) {
    val tokenHandler = JwtTokenHandler(jwtProperties.secret)

    fun decodeToJwt(token: String): Jwt {
        val claims = tokenHandler.parseToken(token)
        return Jwt.withTokenValue(token)
            .header(HEADER_TYPE, JWT_TYPE)
            .header(HEADER_ALGORITHM, Jwts.SIG.HS256.id)
            .claims { claimsMap ->
                claims.forEach { (key, value) ->
                    claimsMap[key] = when (key) {
                        Claims.ISSUED_AT, Claims.EXPIRATION -> Instant.ofEpochSecond((value as Number).toLong())
                        else -> value
                    }
                }
            }
            .build()
    }
}
