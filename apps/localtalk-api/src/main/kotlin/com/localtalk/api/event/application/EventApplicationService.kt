package com.localtalk.api.event.application

import com.localtalk.api.bookmark.domain.BookmarkService
import com.localtalk.api.event.application.dto.EventDetailInfo
import com.localtalk.api.event.application.mapper.EventApplicationMapper
import com.localtalk.api.event.domain.EventService
import com.localtalk.api.review.domain.ReviewService
import com.localtalk.api.visit.domain.VisitService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventApplicationService(
    private val eventService: EventService,
    private val reviewService: ReviewService,
    private val bookmarkService: BookmarkService,
    private val visitService: VisitService,
    private val eventApplicationMapper: EventApplicationMapper,
) {

    @Transactional(readOnly = true)
    fun getEventDetail(eventId: Long, memberId: Long?): EventDetailInfo {
        val event = eventService.getEventByIdOrThrow(eventId)
        val imageUrl = eventService.getImageUrl(event)
        val isLoggedIn = memberId != null
        val isBookmarked = memberId?.let { bookmarkService.isMemberBookmarked(eventId, it) } ?: false
        val isVisited = memberId?.let { visitService.isMemberVisited(eventId, it) } ?: false
        val totalCount = reviewService.getTotalReviewCount(eventId)
        val averageRating = reviewService.getAverageRating(eventId)

        return eventApplicationMapper.toEventDetailInfo(
            event = event,
            imageUrl = imageUrl,
            isLoggedIn = isLoggedIn,
            isBookmarked = isBookmarked,
            isVisited = isVisited,
            totalReviewCount = totalCount.toInt(),
            averageRating = averageRating
        )
    }
}
