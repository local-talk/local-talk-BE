package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.SocialLogin
import com.localtalk.api.auth.domain.SocialLoginProvider
import com.localtalk.api.auth.domain.contract.SocialLoginRepository
import org.springframework.stereotype.Component

@Component
class SocialLoginService(
    private val socialLoginRepository: SocialLoginRepository,
) {

    fun findByProviderAndSocialKey(provider: SocialLoginProvider, socialKey: String): SocialLogin? =
        socialLoginRepository.findByProviderAndSocialKey(provider, socialKey)

    fun create(provider: SocialLoginProvider, socialKey: String): SocialLogin {
        val socialLogin = SocialLogin(
            provider = provider,
            socialKey = socialKey,
        )
        return socialLoginRepository.save(socialLogin)
    }
}
