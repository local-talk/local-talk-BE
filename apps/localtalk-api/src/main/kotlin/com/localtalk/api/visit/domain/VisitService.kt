package com.localtalk.api.visit.domain

import org.springframework.stereotype.Service

@Service
class VisitService(
    private val visitRepository: VisitRepository,
) {

    fun isMemberVisited(eventId: Long, memberId: Long): Boolean {
        return visitRepository.existsByEventIdAndMemberId(eventId, memberId)
    }
}