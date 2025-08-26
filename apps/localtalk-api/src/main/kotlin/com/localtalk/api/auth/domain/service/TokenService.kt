package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.LoginToken
import com.localtalk.api.auth.domain.RefreshToken
import com.localtalk.api.auth.domain.contract.RefreshTokenRepository
import com.localtalk.api.auth.domain.contract.TokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun rotateRefreshToken(refreshToken: String): LoginToken {
        val token = refreshTokenRepository.findByToken(refreshToken)
            ?: throw IllegalArgumentException("존재하지 않는 토큰입니다.")

        val authMember = tokenProvider.parseToken(token.token)

        return tokenProvider.generateToken(authMember.id, authMember.role.name)
            .also { (_, refreshToken) -> token.update(refreshToken) }
            .let { (accessToken, refreshToken) -> LoginToken(accessToken, refreshToken) }
    }

}

