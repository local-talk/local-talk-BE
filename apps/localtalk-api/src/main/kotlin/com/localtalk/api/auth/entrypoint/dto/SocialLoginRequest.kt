package com.localtalk.api.auth.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 로그인 요청")
data class SocialLoginRequest(
    @Schema(description = "소셜 로그인 액세스 토큰", example = "kakao_access_token_example", required = true)
    val accessToken: String?,
) {
    init {
        requireNotNull(accessToken) { "액세스 토큰을 입력해주세요." }
    }
}
