package com.localtalk.api.auth.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 로그인 응답")
data class SocialLoginResponse(
    @field:Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,
    @field:Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
    @field:Schema(description = "가입된 회원 여부 (가입된 경우 true)", example = "true")
    val isSignedUser: Boolean,
)
