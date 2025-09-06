package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.SocialLogin
import com.localtalk.api.auth.domain.SocialLoginProvider
import com.localtalk.api.auth.domain.contract.SocialLoginRepository
import org.springframework.data.repository.findByIdOrNull
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

    fun getUnregisteredSocialLogin(id: Long): SocialLogin {
        val socialLogin = socialLoginRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("존재하지 않는 소셜 로그인 사용자입니다")

        if (socialLogin.isSignedUser()) {
            throw IllegalStateException("이미 회원가입이 완료된 사용자입니다")
        }

        return socialLogin
    }

}
