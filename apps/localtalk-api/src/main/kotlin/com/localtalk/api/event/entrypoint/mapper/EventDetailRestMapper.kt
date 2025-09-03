package com.localtalk.api.event.entrypoint.mapper

import com.localtalk.api.event.application.dto.EventDetailInfo
import com.localtalk.api.event.entrypoint.dto.EventDetailResponse
import org.springframework.stereotype.Component

@Component
class EventDetailRestMapper {

    fun toEventDetailResponse(info: EventDetailInfo): EventDetailResponse {
        return EventDetailResponse(
            title = info.title,
            description = info.description,
            imageUrl = info.imageUrl,
            startDate = info.startDate,
            endDate = info.endDate,
            operatingHours = info.operatingHours,
            location = info.location,
            address = info.address,
            price = info.price,
            contactPhone = info.contactPhone,
            officialWebsite = info.officialWebsite,
            longitude = info.longitude,
            latitude = info.latitude,
            isLoggedIn = info.isLoggedIn,
            isBookmarked = info.isBookmarked,
            isVisited = info.isVisited,
            averageRating = info.averageRating,
            totalCount = info.totalReviewCount,
        )
    }
}
