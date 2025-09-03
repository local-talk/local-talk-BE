package com.localtalk.api.review.domain

import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {

    fun getTotalReviewCount(eventId: Long): Long {
        return reviewRepository.countByEventIdAndDeletedAtIsNull(eventId)
    }

    fun getAverageRating(eventId: Long): Double {
        return reviewRepository.getAverageRatingByEventId(eventId) ?: 0.0
    }
}
