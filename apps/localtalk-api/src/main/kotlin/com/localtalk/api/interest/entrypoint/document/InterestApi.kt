package com.localtalk.api.interest.entrypoint.document

import com.localtalk.api.interest.entrypoint.dto.InterestResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "관심사", description = "관심사 관련 API")
@RequestMapping("/api/v1/interests")
interface InterestApi {

    @Operation(
        summary = "관심사 목록 조회",
        description = "등록된 모든 관심사를 표시 순서대로 조회합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "관심사 목록 조회 성공"),
        ],
    )
    @GetMapping
    fun findAllInterests(): ResponseEntity<RestResponse<List<InterestResponse>>>
}
