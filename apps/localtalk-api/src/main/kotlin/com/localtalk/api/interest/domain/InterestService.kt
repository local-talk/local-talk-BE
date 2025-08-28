package com.localtalk.api.interest.domain

import org.springframework.stereotype.Service

@Service
class InterestService(
    val interestRepository: InterestRepository,
) {
    fun findAllInterests(): List<Interest> =
        interestRepository.findAllByDeletedAtIsNullOrderByDisplayOrder()
}
