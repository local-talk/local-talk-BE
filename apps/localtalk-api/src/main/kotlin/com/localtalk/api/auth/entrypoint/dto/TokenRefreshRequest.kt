package com.localtalk.api.auth.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "리프레시 토큰 갱신 요청")
data class TokenRefreshRequest(
    @field:Schema(
        description = "갱신할 리프레시 토큰",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        required = true
    )
    val refreshToken: String?
) {
    init {
        require(refreshToken != null) { "리프레시 토큰을 입력해주세요" }
    }
}
