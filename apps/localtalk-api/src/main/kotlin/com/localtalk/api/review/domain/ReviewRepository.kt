package com.localtalk.api.review.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : JpaRepository<Review, Long> {

    fun countByEventIdAndDeletedAtIsNull(eventId: Long): Long

    @Query("SELECT ROUND(AVG(r.rating), 1) FROM Review r WHERE r.event.id = :eventId AND r.deletedAt IS NULL")
    fun getAverageRatingByEventId(@Param("eventId") eventId: Long): Double?
}
