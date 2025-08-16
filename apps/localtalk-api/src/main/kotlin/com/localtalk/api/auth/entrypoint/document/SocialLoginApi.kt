package com.localtalk.api.auth.entrypoint.document

import com.localtalk.api.auth.entrypoint.dto.SocialLoginRequest
import com.localtalk.api.auth.entrypoint.dto.SocialLoginResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "소셜 로그인", description = "소셜 로그인 관련 API")
interface SocialLoginApi {

    @Operation(
        summary = "소셜 로그인",
        description = "카카오, 애플 등 소셜 로그인 프로바이더를 통한 로그인 처리",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "소셜 로그인 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun socialLogin(
        @Parameter(
            description = "소셜 로그인 프로바이더 (kakao, apple)",
            example = "kakao",
            required = true,
        )
        @PathVariable provider: String,

        @RequestBody request: SocialLoginRequest,
    ): ResponseEntity<RestResponse<SocialLoginResponse>>
}
