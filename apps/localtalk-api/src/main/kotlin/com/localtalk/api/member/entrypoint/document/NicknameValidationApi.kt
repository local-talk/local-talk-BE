package com.localtalk.api.member.entrypoint.document

import com.localtalk.api.member.entrypoint.dto.NicknameValidationRequest
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "닉네임 검증", description = "닉네임 유효성 검증 관련 API")
interface NicknameValidationApi {

    @Operation(
        summary = "닉네임 유효성 검증",
        description = "회원가입 시 사용할 닉네임의 형식과 중복 여부를 검증합니다. " +
            "검증 규칙: 2-12자, 한글/영문/숫자/공백/언더스코어만 허용, " +
            "공백이나 언더스코어로 시작/끝 불가, 연속 사용 불가, 중복 불가",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "닉네임 검증 성공 (사용 가능한 닉네임)",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "검증 성공",
                                description = "사용 가능한 닉네임",
                                value = "{\"code\":200,\"data\":{},\"message\":\"닉네임 검증이 완료되었습니다\"}",
                            ),
                        ],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "닉네임 검증 실패",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "닉네임 누락",
                                description = "닉네임이 전달되지 않은 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임은 필수입니다\"}",
                            ),
                            ExampleObject(
                                name = "길이 오류 - 너무 짧음",
                                description = "2자 미만인 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임은 2자 이상 12자 이하여야 합니다\"}",
                            ),
                            ExampleObject(
                                name = "길이 오류 - 너무 김",
                                description = "12자 초과인 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임은 2자 이상 12자 이하여야 합니다\"}",
                            ),
                            ExampleObject(
                                name = "허용되지 않는 문자",
                                description = "특수문자 등 허용되지 않는 문자 포함",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임은 한글, 영문, 숫자, 공백, _만 사용 가능합니다\"}",
                            ),
                            ExampleObject(
                                name = "잘못된 시작/끝 문자",
                                description = "공백이나 _로 시작하거나 끝나는 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임은 공백이나 _로 시작할 수 없습니다\"}",
                            ),
                            ExampleObject(
                                name = "연속 특수문자",
                                description = "공백이나 _를 연속으로 사용한 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"닉네임에 공백이나 _를 연속으로 사용할 수 없습니다\"}",
                            ),
                            ExampleObject(
                                name = "중복된 닉네임",
                                description = "이미 사용 중인 닉네임인 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"이미 사용 중인 닉네임입니다\"}",
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
    fun validateNickname(
        @RequestBody request: NicknameValidationRequest,
    ): ResponseEntity<RestResponse<Unit>>
}
