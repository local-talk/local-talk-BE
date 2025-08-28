package com.localtalk.api.event.domain

import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
) {

    fun findByIdActive(eventId: Long): Event? {
        return eventRepository.findByIdActive(eventId)
    }

    fun isMemberBookmarked(eventId: Long, memberId: Long): Boolean {
        return eventRepository.existsBookmarkByEventIdAndMemberId(eventId, memberId)
    }

    fun isMemberVisited(eventId: Long, memberId: Long): Boolean {
        return eventRepository.existsVisitByEventIdAndMemberId(eventId, memberId)
    }

    fun getAverageRating(eventId: Long): Double {
        return eventRepository.getAverageRatingByEvent(eventId)
    }

    fun getTotalReviewCount(eventId: Long): Long {
        return eventRepository.countReviewsByEvent(eventId)
    }
}
