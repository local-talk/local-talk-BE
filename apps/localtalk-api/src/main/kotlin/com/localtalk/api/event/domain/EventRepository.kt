package com.localtalk.api.event.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EventRepository : JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.deletedAt IS NULL")
    fun findByIdActive(@Param("eventId") eventId: Long): Event?

    fun existsBookmarkByEventIdAndMemberId(eventId: Long, memberId: Long): Boolean

    fun existsVisitByEventIdAndMemberId(eventId: Long, memberId: Long): Boolean

    @Query("SELECT COUNT(r) FROM Review r WHERE r.event.id = :eventId AND r.deletedAt IS NULL")
    fun countReviewsByEvent(@Param("eventId") eventId: Long): Long

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.event.id = :eventId AND r.deletedAt IS NULL")
    fun getAverageRatingByEvent(@Param("eventId") eventId: Long): Double
}
