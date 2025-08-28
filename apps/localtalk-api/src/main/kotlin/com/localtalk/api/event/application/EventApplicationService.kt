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
        val event = eventService.findByIdActive(eventId)
            ?: throw IllegalArgumentException("존재하지 않는 행사입니다")

        val imageUrl = getMainImageUrl(event)
        val isLoggedIn = memberId != null
        val isBookmarked = memberId?.let { eventService.isUserBookmarked(eventId, it) } ?: false
        val isVisited = memberId?.let { eventService.isUserVisited(eventId, it) } ?: false
        val (averageRating, totalCount) = eventService.getReviewSummary(eventId)

        return eventApplicationMapper.toEventDetailInfo(
            event = event,
            imageUrl = imageUrl,
            isLoggedIn = isLoggedIn,
            isBookmarked = isBookmarked,
            isVisited = isVisited,
            averageRating = averageRating,
            totalReviewCount = totalCount.toInt()
        )
    }

    private fun getMainImageUrl(event: Event): String {
        return "/images/default-event.jpg" // TODO: EventImage 연관관계 설정 후 구현
    }
}
