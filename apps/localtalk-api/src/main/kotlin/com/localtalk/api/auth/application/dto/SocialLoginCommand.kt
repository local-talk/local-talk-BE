package com.localtalk.api.auth.application.dto

import com.localtalk.api.auth.domain.SocialLoginProvider

data class SocialLoginCommand(
    val provider: SocialLoginProvider,
    val accessToken: String,
) {
    init {
        require(accessToken.isNotBlank()) { "액세스 토큰은 빈 값일 수 없습니다." }
    }
}
