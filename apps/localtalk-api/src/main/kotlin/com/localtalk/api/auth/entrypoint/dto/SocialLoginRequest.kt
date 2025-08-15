package com.localtalk.api.auth.entrypoint.dto

data class SocialLoginRequest(
    val accessToken: String?,
) {
    init {
        requireNotNull(accessToken) { "액세스 토큰을 입력해주세요." }
    }
}
