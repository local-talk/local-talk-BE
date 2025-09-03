package com.localtalk.api.bookmark.domain

import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {

    fun existsByEventIdAndMemberId(eventId: Long, memberId: Long): Boolean
}
