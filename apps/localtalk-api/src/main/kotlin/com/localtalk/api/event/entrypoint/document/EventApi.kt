package com.localtalk.api.event.entrypoint.document

import com.localtalk.api.event.entrypoint.dto.EventDetailResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "행사", description = "행사 관련 API")
interface EventApi {

    @Operation(
        summary = "행사 상세 정보 조회",
        description = "특정 행사의 상세 정보를 조회합니다. 회원인 경우 북마크/방문 상태가 포함되어 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "행사 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "성공 응답",
                                description = "행사 상세 정보 조회 성공",
                                value = """{
  "code": 200,
  "data": {
    "title": "성북구 겨울 축제",
    "description": "성북구에서 열리는 겨울 축제입니다.",
    "imageUrl": "https://example/default.jpg"
    "startDate": "2024-12-01",
    "endDate": "2024-12-31",
    "operatingHours": "10:00~18:00",
    "location": "성북구청 앞 광장",
    "address": "서울특별시 성북구 보문로 168",
    "price": 0,
    "contactPhone": "02-1234-5678",
    "officialWebsite": "https://seongbuk.go.kr",
    "latitude": 37.5665,
    "longitude": 127.0167,
    "isLoggedIn": true,
    "isBookmarked": false,
    "isVisited": false,
    "averageRating": 4.5,
    "totalCount": 12
  },
  "message": "행사 상세 정보를 성공적으로 조회했습니다"
}"""
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "존재하지 않는 행사",
                                description = "요청한 행사 ID가 존재하지 않는 경우",
                                value = "{\"code\":400,\"data\":{},\"message\":\"존재하지 않는 행사입니다\"}"
                            )
                        ]
                    )
                ]
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
                                value = "{\"code\":500,\"data\":{},\"message\":\"서버 내부 오류가 발생했습니다.\"}"
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun getEventDetail(
        @Schema(description = "조회할 행사 ID", example = "1")
        @PathVariable eventId: Long
    ): ResponseEntity<RestResponse<EventDetailResponse>>
}
