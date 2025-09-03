package com.localtalk.api.event.domain

import com.localtalk.api.infrastructure.storage.S3ImageUrlGenerator
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val s3ImageUrlGenerator: S3ImageUrlGenerator,
) {

    fun getEventByIdOrThrow(eventId: Long): Event {
        return eventRepository.findByIdAndDeletedAtIsNull(eventId) ?: throw IllegalArgumentException("존재하지 않는 행사입니다.")
    }

    fun getImageUrl(event: Event): String {
        return s3ImageUrlGenerator.generateImageUrl(event.eventImageKey)
    }
}
