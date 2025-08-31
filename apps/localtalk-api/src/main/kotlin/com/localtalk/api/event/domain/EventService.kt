package com.localtalk.api.event.domain

import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
) {

    fun getEventByIdOrThrow(eventId: Long): Event {
        return eventRepository.findByIdActive(eventId) ?: throw IllegalArgumentException("존재하지 않는 행사입니다.")
    }
}
