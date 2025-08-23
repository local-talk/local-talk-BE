package com.localtalk.api.member.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "닉네임 검증 요청")
data class NicknameValidationRequest(
    @field:Schema(
        description = "검증할 닉네임 (한글, 영문, 숫자, 공백, _ 허용, 2-12자)",
        example = "홍길동",
        required = true
    )
    val nickname: String?,
) {
    init {
        requireNotNull(nickname) { "닉네임은 필수입니다" }
    }
}
