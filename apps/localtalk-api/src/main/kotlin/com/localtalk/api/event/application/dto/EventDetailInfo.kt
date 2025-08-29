package com.localtalk.api.event.application.dto

import java.math.BigDecimal
import java.time.LocalDate

data class EventDetailInfo(
    val title: String,
    val description: String?,
    val imageUrl: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val operatingHours: String?,
    val location: String?,
    val address: String?,
    val price: Int,
    val contactPhone: String?,
    val officialWebsite: String?,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,

    val isLoggedIn: Boolean,
    val isBookmarked: Boolean,
    val isVisited: Boolean,

    val averageRating: Double,
    val totalReviewCount: Int
)
