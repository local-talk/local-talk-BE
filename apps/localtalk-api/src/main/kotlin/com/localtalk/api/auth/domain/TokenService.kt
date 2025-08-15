package com.localtalk.api.auth.domain

import org.springframework.stereotype.Service

@Service
class TokenService(
    val tokenProvider: TokenProvider,
    val loginTokenRepository: LoginTokenRepository,
) {

    fun generateToken(id: Long, role: Role): LoginToken {
        val (accessToken, refreshToken) = tokenProvider.generateToken(id, role)
        val loginToken = LoginToken(accessToken, refreshToken)
        return loginTokenRepository.save(loginToken)
    }

}
