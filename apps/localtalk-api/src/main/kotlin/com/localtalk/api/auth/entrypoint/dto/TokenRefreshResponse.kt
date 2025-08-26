package com.localtalk.api.auth.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "리프레시 토큰 갱신 응답")
data class TokenRefreshResponse(
    @field:Schema(
        description = "새로 발급된 액세스 토큰",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiYXV0aFJvbGUiOiJNRU1CRVIiLCJpYXQiOjE3MDMxNjk2MDAsImV4cCI6MTcwMzE3MzIwMH0.new_access_token_signature"
    )
    val accessToken: String,

    @field:Schema(
        description = "업데이트된 리프레시 토큰 (기존 토큰이 갱신됨)",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidHlwZSI6InJlZnJlc2giLCJpYXQiOjE3MDMxNjk2MDAsImV4cCI6MTcwNDQ2NTYwMH0.updated_refresh_token_signature"
    )
    val refreshToken: String
)