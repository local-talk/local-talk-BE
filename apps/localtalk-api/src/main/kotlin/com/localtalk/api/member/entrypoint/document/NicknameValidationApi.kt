package com.localtalk.api.member.entrypoint.document

import com.localtalk.api.member.entrypoint.dto.NicknameValidationRequest
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "닉네임 검증", description = "닉네임 유효성 검증 관련 API")
interface NicknameValidationApi {

    @Operation(
        summary = "닉네임 유효성 검증",
        description = """
            닉네임의 형식과 중복 여부를 검증합니다.
            
            ## 검증 규칙
            - **길이**: 2자 이상 12자 이하
            - **허용 문자**: 한글, 영문, 숫자, 공백, 언더스코어(_)
            - **위치 제약**: 공백이나 _로 시작/끝날 수 없음
            - **연속 제약**: 공백이나 _를 연속으로 사용할 수 없음
            - **중복 검증**: 이미 다른 회원이 사용 중인 닉네임은 사용 불가
        """,
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "닉네임 검증 성공 (사용 가능한 닉네임)"),
            ApiResponse(responseCode = "400", description = "닉네임 검증 실패 (형식 오류 또는 중복)"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun validateNickname(
        @RequestBody request: NicknameValidationRequest,
    ): ResponseEntity<RestResponse<Unit>>
}