package com.localtalk.api.visit.domain

import org.springframework.data.jpa.repository.JpaRepository

interface VisitRepository : JpaRepository<Visit, Long> {

    fun existsByEventIdAndMemberId(eventId: Long, memberId: Long): Boolean
}