package com.localtalk.api.event.application

import com.localtalk.api.event.application.dto.EventDetailInfo
import com.localtalk.api.event.application.mapper.EventApplicationMapper
import com.localtalk.api.event.domain.Event
import com.localtalk.api.event.domain.EventService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventApplicationService(
    private val eventService: EventService,
    private val eventApplicationMapper: EventApplicationMapper,
) {

    @Transactional(readOnly = true)
    fun getEventDetail(eventId: Long, memberId: Long?): EventDetailInfo {
        val event = findEventOrThrow(eventId)
        val totalCount = eventService.getTotalReviewCount(eventId)
        val averageRating = calculateAverageRating(totalCount, eventId)

        return eventApplicationMapper.toEventDetailInfo(
            event = event,
            imageUrl = getImageUrl(event),
            isLoggedIn = memberId != null,
            isBookmarked = memberId?.let { eventService.isMemberBookmarked(eventId, it) } ?: false,
            isVisited = memberId?.let { eventService.isMemberVisited(eventId, it) } ?: false,
            totalReviewCount = totalCount.toInt(),
            averageRating = averageRating
        )
    }

    private fun findEventOrThrow(eventId: Long): Event =
        eventService.findByIdActive(eventId) ?: throw IllegalArgumentException("존재하지 않는 행사입니다")

    private fun calculateAverageRating(totalCount: Long, eventId: Long): Double =
        if (totalCount > 0) eventService.getAverageRating(eventId) else 0.0

    private fun getImageUrl(event: Event): String {
        return "https://example/default.jpg" // TODO: S3 설정 후 구현
    }
}
