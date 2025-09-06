package com.localtalk.api.member.entrypoint.document

import com.localtalk.api.auth.domain.AuthMember
import com.localtalk.api.member.entrypoint.dto.SignupRequest
import com.localtalk.api.member.entrypoint.dto.SignupResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "회원가입", description = "회원가입 관련 API")
interface SignupApi {

    @Operation(
        summary = "회원가입",
        description = "소셜 로그인 완료 후 추가 정보를 입력하여 정식 회원가입을 완료합니다. " +
            "TEMPORARY 권한을 가진 사용자만 접근 가능하며, 회원가입 완료 후 MEMBER 권한의 새로운 토큰을 발급받습니다. " +
            "닉네임, 생년월일, 성별, 거주지, 마케팅 동의 여부는 필수이며, 프로필 이미지와 관심사는 선택사항입니다.",
        security = [io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "회원가입 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "회원가입 성공",
                                description = "정상적으로 회원가입이 완료되고 MEMBER 권한 토큰 발급",
                                value = "{\"code\":200,\"data\":{\"access_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\"refresh_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"},\"message\":\"회원가입이 완료되었습니다\"}",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun signup(
        @RequestBody request: SignupRequest,
        @AuthenticationPrincipal authMember: AuthMember,
    ): ResponseEntity<RestResponse<SignupResponse>>
}
