package com.localtalk.api.auth.entrypoint.mapper

import com.localtalk.api.auth.application.SocialLoginCommand
import com.localtalk.api.auth.application.SocialLoginInfo
import com.localtalk.api.auth.domain.SocialLoginProvider
import com.localtalk.api.auth.entrypoint.dto.SocialLoginRequest
import com.localtalk.api.auth.entrypoint.dto.SocialLoginResponse
import org.springframework.stereotype.Component

@Component
class SocialLoginRestMapper {

    fun toCommand(
        request: SocialLoginRequest,
        provider: SocialLoginProvider,
    ): SocialLoginCommand = SocialLoginCommand(
        provider = provider,
        accessToken = request.accessToken!!,
    )

    fun toResponse(info: SocialLoginInfo): SocialLoginResponse =
        SocialLoginResponse(
            accessToken = info.accessToken,
            refreshToken = info.refreshToken,
            isSignedUser = info.isSignedUser,
        )

}
