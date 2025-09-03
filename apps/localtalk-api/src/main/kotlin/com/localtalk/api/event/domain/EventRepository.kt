package com.localtalk.api.event.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EventRepository : JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.deletedAt IS NULL")
    fun findByIdAndDeletedAtIsNull(@Param("eventId") eventId: Long): Event?
}
