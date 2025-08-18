package com.localtalk.api.auth.application

import com.localtalk.api.auth.application.dto.SocialLoginCommand
import com.localtalk.api.auth.application.dto.SocialLoginInfo
import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.contract.ExternalTokenValidator
import com.localtalk.api.auth.domain.service.SocialLoginService
import com.localtalk.api.auth.domain.service.TokenService
import org.springframework.stereotype.Service

@Service
class SocialLoginApplicationService(
    val socialLoginService: SocialLoginService,
    val tokenService: TokenService,
    val externalTokenValidator: ExternalTokenValidator,
) {

    suspend fun processSocialLogin(command: SocialLoginCommand): SocialLoginInfo {
        val validationResult = externalTokenValidator.validateToken(command.accessToken, command.provider.name)

        val socialLogin = socialLoginService.findByProviderAndSocialKey(command.provider, validationResult.socialKey)
            ?: socialLoginService.create(command.provider, validationResult.socialKey)

        val loginToken = socialLogin.takeIf { it.isSignedUser() }
            ?.let { tokenService.generateToken(id = it.memberId!!, authRole = AuthRole.MEMBER) }
            ?: tokenService.generateToken(id = socialLogin.id, authRole = AuthRole.TEMPORARY)

        return SocialLoginInfo(
            accessToken = loginToken.accessToken,
            refreshToken = loginToken.refreshToken,
            isSignedUser = socialLogin.isSignedUser(),
        )
    }

}
