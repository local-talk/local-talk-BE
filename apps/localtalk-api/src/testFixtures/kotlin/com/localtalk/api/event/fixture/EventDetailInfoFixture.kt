package com.localtalk.api.event.fixture

import com.localtalk.api.event.application.dto.EventDetailInfo
import java.math.BigDecimal
import java.time.LocalDate

object EventDetailInfoFixture {

    fun createEventDetailInfo(
        title: String = "테스트 행사 제목",
        description: String? = "테스트 행사 설명",
        imageUrl: String = "https://example.com/image.jpg",
        startDate: LocalDate = LocalDate.of(2025, 8, 29),
        endDate: LocalDate? = LocalDate.of(2025, 8, 31),
        operatingHours: String? = "10:00~18:00",
        location: String? = "테스트 장소",
        address: String? = "서울시 테스트구 테스트동",
        price: Int = 5000,
        contactPhone: String? = "02-1234-5678",
        officialWebsite: String? = "https://test.com",
        latitude: BigDecimal? = null,
        longitude: BigDecimal? = null,
        isLoggedIn: Boolean = false,
        isBookmarked: Boolean = false,
        isVisited: Boolean = false,
        averageRating: Double = 4.5,
        totalReviewCount: Int = 10
    ): EventDetailInfo {
        return EventDetailInfo(
            title = title,
            description = description,
            imageUrl = imageUrl,
            startDate = startDate,
            endDate = endDate,
            operatingHours = operatingHours,
            location = location,
            address = address,
            price = price,
            contactPhone = contactPhone,
            officialWebsite = officialWebsite,
            latitude = latitude,
            longitude = longitude,
            isLoggedIn = isLoggedIn,
            isBookmarked = isBookmarked,
            isVisited = isVisited,
            averageRating = averageRating,
            totalReviewCount = totalReviewCount
        )
    }
}