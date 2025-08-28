package com.localtalk.api.interest.domain

import org.springframework.data.jpa.repository.JpaRepository

interface InterestRepository : JpaRepository<Interest, Long> {
    fun findAllByDeletedAtIsNullOrderByDisplayOrder(): List<Interest>
}