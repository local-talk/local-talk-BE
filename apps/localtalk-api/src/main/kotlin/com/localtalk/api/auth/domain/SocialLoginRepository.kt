package com.localtalk.api.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface SocialLoginRepository : JpaRepository<SocialLogin, Long> {
    fun findByProviderAndSocialKey(provider: SocialLoginProvider, socialKey: String): SocialLogin?
}
