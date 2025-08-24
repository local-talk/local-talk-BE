package com.localtalk.api.district.entrypoint.document

import com.localtalk.api.district.entrypoint.dto.DongResponse
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

@Tag(name = "지역 정보", description = "지역(구/동) 정보 관련 API")
interface DistrictApi {

    @Operation(
        summary = "동 목록 조회",
        description = "지정된 구의 동 목록을 법정동 코드와 함께 조회합니다. 현재 성북구(seongbuk)만 지원됩니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", 
                description = "동 목록 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "성공 응답",
                        description = "성북구 동 목록 조회 성공",
                        value = "{\"code\":200,\"data\":[{\"name\":\"길음1동\",\"legal_code\":\"1129010100\"},{\"name\":\"길음2동\",\"legal_code\":\"1129010200\"},{\"name\":\"동선동\",\"legal_code\":\"1129010300\"}],\"message\":\"성북구 동 목록을 성공적으로 조회했습니다\"}"
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400", 
                description = "잘못된 요청 - 지원하지 않는 구 이름",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "지원하지 않는 구 오류",
                        description = "gangnam과 같이 지원하지 않는 구 요청시",
                        value = "{\"code\":400,\"data\":{},\"message\":\"지원하지 않는 구입니다: gangnam\"}"
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "500", 
                description = "서버 내부 오류",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "서버 오류",
                        description = "예상치 못한 서버 내부 오류",
                        value = "{\"code\":500,\"data\":{},\"message\":\"서버 내부 오류가 발생했습니다.\"}"
                    )]
                )]
            ),
        ],
    )
    fun findDongs(
        @Parameter(
            description = "구 이름 (영문)",
            example = "seongbuk",
            required = true,
        )
        @PathVariable district: String,
    ): ResponseEntity<RestResponse<List<DongResponse>>>
}