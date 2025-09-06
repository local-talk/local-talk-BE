package com.localtalk.api.member.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원가입 응답")
data class SignupResponse(
    @field:Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,
    @field:Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
)