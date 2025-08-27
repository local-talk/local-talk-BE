package com.localtalk.api.auth.entrypoint.document

import com.localtalk.api.auth.entrypoint.dto.SocialLoginRequest
import com.localtalk.api.auth.entrypoint.dto.SocialLoginResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
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
        description = "카카오, 애플 등 소셜 로그인 프로바이더를 통한 로그인을 처리합니다. " +
                "클라이언트에서 획득한 액세스 토큰을 검증하여 애플리케이션 전용 토큰을 발급합니다. " +
                "지원 프로바이더: kakao, apple",
        security = [],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "소셜 로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "기존 회원 로그인 성공",
                                description = "이미 가입된 회원의 로그인 성공",
                                value = "{\"code\":200,\"data\":{\"access_token\":\"eyJhbGci...\",\"refresh_token\":\"eyJhbGci...\",\"is_new_user\":false},\"message\":\"소셜 로그인이 성공했습니다\"}",
                            ),
                            ExampleObject(
                                name = "신규 회원 가입 및 로그인 성공",
                                description = "새로운 회원의 가입 및 로그인 성공",
                                value = "{\"code\":200,\"data\":{\"access_token\":\"eyJhbGci...\",\"refresh_token\":\"eyJhbGci...\",\"is_new_user\":true},\"message\":\"소셜 로그인이 성공했습니다\"}",
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "지원하지 않는 프로바이더",
                                description = "kakao, apple 이외의 프로바이더 요청시",
                                value = "{\"code\":400,\"data\":{},\"message\":\"지원하지 않는 소셜 로그인 프로바이더입니다: google\"}",
                            ),
                            ExampleObject(
                                name = "액세스 토큰 누락",
                                description = "요청에 액세스 토큰이 포함되지 않은 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"액세스 토큰을 입력해주세요.\"}",
                            ),
                            ExampleObject(
                                name = "유효하지 않은 액세스 토큰",
                                description = "프로바이더에서 토큰 검증에 실패한 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"유효하지 않은 액세스 토큰입니다\"}",
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "서버 오류",
                                description = "예상치 못한 서버 내부 오류 발생",
                                value = "{\"code\":500,\"data\":{},\"message\":\"서버 내부 오류가 발생했습니다.\"}",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    suspend fun socialLogin(
        @Parameter(
            description = "소셜 로그인 프로바이더",
            example = "kakao",
            required = true,
        )
        @PathVariable provider: String,

        @RequestBody request: SocialLoginRequest,
    ): ResponseEntity<RestResponse<SocialLoginResponse>>
}
