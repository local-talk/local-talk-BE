package com.localtalk.api.event.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "행사 상세조회 응답")
data class EventDetailResponse(
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
    val totalCount: Int
)
