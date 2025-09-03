package com.localtalk.api.bookmark.domain

import org.springframework.stereotype.Service

@Service
class BookmarkService(
    private val bookmarkRepository: BookmarkRepository,
) {

    fun isMemberBookmarked(eventId: Long, memberId: Long): Boolean {
        return bookmarkRepository.existsByEventIdAndMemberId(eventId, memberId)
    }
}