package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.SocialLogin
import com.localtalk.api.auth.domain.SocialLoginProvider
import org.springframework.data.jpa.repository.JpaRepository

interface SocialLoginRepository : JpaRepository<SocialLogin, Long> {
    fun findByProviderAndSocialKey(provider: SocialLoginProvider, socialKey: String): SocialLogin?
}
