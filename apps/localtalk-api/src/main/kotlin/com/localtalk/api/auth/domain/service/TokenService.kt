package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.LoginToken
import com.localtalk.api.auth.domain.RefreshToken
import com.localtalk.api.auth.domain.contract.RefreshTokenRepository
import com.localtalk.api.auth.domain.contract.TokenProvider
import org.springframework.stereotype.Service

@Service
class TokenService(
    val tokenProvider: TokenProvider,
    val refreshTokenRepository: RefreshTokenRepository,
) {

    fun generateToken(id: Long, authRole: AuthRole): LoginToken {
        return tokenProvider.generateToken(id, authRole.name)
            .also { (_, refreshToken) -> refreshTokenRepository.save(RefreshToken(refreshToken)) }
            .let { (accessToken, refreshToken) -> LoginToken(accessToken, refreshToken) }
    }

}

