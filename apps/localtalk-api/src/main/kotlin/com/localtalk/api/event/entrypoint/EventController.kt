package com.localtalk.api.event.entrypoint

import com.localtalk.api.event.application.EventApplicationService
import com.localtalk.api.event.entrypoint.document.EventApi
import com.localtalk.api.event.entrypoint.dto.EventDetailResponse
import com.localtalk.api.event.entrypoint.mapper.EventDetailRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class EventController(
    private val eventApplicationService: EventApplicationService,
    private val eventDetailRestMapper: EventDetailRestMapper
) : EventApi {

    @GetMapping("/events/{eventId}")
    override fun getEventDetail(
        @PathVariable eventId: Long,
        @RequestHeader(value = "Authorization", required = false) authorization: String?
    ): ResponseEntity<RestResponse<EventDetailResponse>> {

        // TODO: JWT 토큰에서 memberId 추출하는 로직 필요
        val memberId: Long? = extractMemberIdFromToken(authorization)

        return eventApplicationService.getEventDetail(eventId, memberId)
            .let { eventDetailInfo -> eventDetailRestMapper.toEventDetailResponse(eventDetailInfo) }
            .let { eventDetailResponse -> RestResponse.success(eventDetailResponse, "행사 상세 정보를 성공적으로 조회했습니다") }
            .let { ResponseEntity.ok(it) }
    }

    private fun extractMemberIdFromToken(authorization: String?): Long? {
        // TODO: JWT 토큰 파싱 로직 구현
        return null
    }
}
