package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.LoginToken
import com.localtalk.api.auth.domain.contract.LoginTokenRepository
import com.localtalk.api.auth.domain.contract.TokenProvider
import org.springframework.stereotype.Service

@Service
class TokenService(
    val tokenProvider: TokenProvider,
    val loginTokenRepository: LoginTokenRepository,
) {

    fun generateToken(id: Long, authRole: AuthRole): LoginToken {
        val (accessToken, refreshToken) = tokenProvider.generateToken(id, authRole.name)
        val loginToken = LoginToken(accessToken, refreshToken)
        return loginTokenRepository.save(loginToken)
    }

}
