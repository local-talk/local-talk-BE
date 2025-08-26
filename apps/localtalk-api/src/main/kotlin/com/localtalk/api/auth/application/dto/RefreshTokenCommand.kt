package com.localtalk.api.auth.application.dto

data class RefreshTokenCommand(
    val refreshToken: String,
) {
    init {
        require(refreshToken.isNotBlank()) { "유효한 리프레시 토큰을 입력해주세요" }
    }
}