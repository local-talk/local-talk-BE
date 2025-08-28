package com.localtalk.api.event.application.mapper

import com.localtalk.api.event.application.dto.EventDetailInfo
import com.localtalk.api.event.domain.Event
import org.springframework.stereotype.Component

@Component
class EventApplicationMapper {
    fun toEventDetailInfo(
        event: Event,
        imageUrl: String,
        isLoggedIn: Boolean,
        isBookmarked: Boolean,
        isVisited: Boolean,
        averageRating: Double,
        totalReviewCount: Int
    ): EventDetailInfo {
        return EventDetailInfo(
            title = event.title,
            description = event.description,
            imageUrl = imageUrl,
            startDate = event.startDate,
            endDate = event.endDate,
            operatingHours = event.operatingHours,
            location = event.location,
            address = event.address,
            price = event.price,
            contactPhone = event.contactPhone,
            officialWebsite = event.officialWebsite,
            longitude = event.longitude,
            latitude = event.latitude,
            isLoggedIn = isLoggedIn,
            isBookmarked = isBookmarked,
            isVisited = isVisited,
            averageRating = averageRating,
            totalReviewCount = totalReviewCount,
        )
    }
}
