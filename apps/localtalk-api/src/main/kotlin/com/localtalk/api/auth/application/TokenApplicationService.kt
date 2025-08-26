package com.localtalk.api.auth.application

import com.localtalk.api.auth.application.dto.RefreshTokenCommand
import com.localtalk.api.auth.application.dto.TokenRefreshInfo
import com.localtalk.api.auth.domain.service.TokenService
import org.springframework.stereotype.Service

@Service
class TokenApplicationService(
    val tokenService: TokenService,
) {
    fun rotateRefreshToken(command: RefreshTokenCommand): TokenRefreshInfo {
        val loginToken = tokenService.rotateRefreshToken(command.refreshToken)

        return TokenRefreshInfo(
            accessToken = loginToken.accessToken,
            refreshToken = loginToken.refreshToken,
        )
    }
}
