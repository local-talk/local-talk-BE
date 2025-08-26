package com.localtalk.api.auth.entrypoint.document

import com.localtalk.api.auth.entrypoint.dto.TokenRefreshRequest
import com.localtalk.api.auth.entrypoint.dto.TokenRefreshResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "인증 관리", description = "토큰 갱신 및 인증 관련 API")
interface AuthApi {

    @Operation(
        summary = "리프레시 토큰 갱신",
        description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다. 기존 리프레시 토큰은 새로운 토큰으로 업데이트됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "토큰 갱신 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "성공 응답",
                        value = "{\"code\":200,\"data\":{\"access_token\":\"eyJhbGci...\",\"refresh_token\":\"eyJhbGci...\"},\"message\":\"토큰이 성공적으로 갱신되었습니다\"}"
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400", 
                description = "잘못된 요청",
                content = [Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "리프레시 토큰 누락",
                            value = "{\"code\":400,\"data\":{},\"message\":\"리프레시 토큰을 입력해주세요\"}"
                        ),
                        ExampleObject(
                            name = "빈 리프레시 토큰",
                            value = "{\"code\":400,\"data\":{},\"message\":\"유효한 리프레시 토큰을 입력해주세요\"}"
                        ),
                        ExampleObject(
                            name = "존재하지 않는 토큰",
                            value = "{\"code\":400,\"data\":{},\"message\":\"존재하지 않는 토큰입니다.\"}"
                        ),
                        ExampleObject(
                            name = "만료된 토큰",
                            value = "{\"code\":400,\"data\":{},\"message\":\"만료된 토큰입니다.\"}"
                        ),
                        ExampleObject(
                            name = "잘못된 요청 형식",
                            value = "{\"code\":400,\"data\":{},\"message\":\"잘못된 요청 형식입니다.\"}"
                        )
                    ]
                )]
            ),
            ApiResponse(
                responseCode = "500", 
                description = "서버 내부 오류",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "서버 내부 오류",
                        value = "{\"code\":500,\"data\":{},\"message\":\"서버 내부 오류가 발생했습니다.\"}"
                    )]
                )]
            )
        ]
    )
    fun rotateRefreshToken(
        @RequestBody request: TokenRefreshRequest
    ): ResponseEntity<RestResponse<TokenRefreshResponse>>
}