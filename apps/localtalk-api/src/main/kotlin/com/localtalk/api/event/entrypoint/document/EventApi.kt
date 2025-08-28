package com.localtalk.api.event.entrypoint.document

import com.localtalk.api.event.entrypoint.dto.EventDetailResponse
import com.localtalk.api.member.entrypoint.dto.NicknameValidationRequest
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@Tag(name = "행사", description = "행사 관련 API")
interface EventApi {

    @Operation(
        summary = "행사 상세페이지 조회",
        description = """
            행사 상세페이지를 조회합니다.

            ## 응답 정보
           - 기본 정보: 제목, 설명, 일정, 위치, 가격 등
           - 사용자 상태: 북마크 여부, 방문완료 여부 (회원일 경우)
           - 연락처 정보: 예약문의 전화번호, 공식사이트 (있는 경우만)
           - 지도 정보: 위치 좌표 (있는 경우만)
           - 리뷰 요약: 평균 평점, 총 리뷰 수 (리뷰가 1개 이상일 때만)

           ## 인증
           - Access Token 없음: 기본 행사 정보만 제공
           - Access Token 있음: 사용자별 상태 정보 포함 제공
        """,
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "행사 조회 성공"),
            ApiResponse(responseCode = "400", description = "존재하지 않는 행사"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun getEventDetail(
        @PathVariable eventId: Long,
        @RequestHeader(value = "Authorization", required = false) authorization: String?
    ): ResponseEntity<RestResponse<EventDetailResponse>>
}
