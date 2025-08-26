package com.localtalk.api.auth.entrypoint.mapper

import com.localtalk.api.auth.application.dto.RefreshTokenCommand
import com.localtalk.api.auth.application.dto.TokenRefreshInfo
import com.localtalk.api.auth.entrypoint.dto.TokenRefreshRequest
import com.localtalk.api.auth.entrypoint.dto.TokenRefreshResponse
import org.springframework.stereotype.Component

@Component
class AuthRestMapper {

    fun toCommand(request: TokenRefreshRequest): RefreshTokenCommand {
        return RefreshTokenCommand(
            refreshToken = request.refreshToken!!
        )
    }

    fun toResponse(info: TokenRefreshInfo): TokenRefreshResponse {
        return TokenRefreshResponse(
            accessToken = info.accessToken,
            refreshToken = info.refreshToken
        )
    }
}