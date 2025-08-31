package com.localtalk.api.event.application

import com.localtalk.api.bookmark.domain.BookmarkService
import com.localtalk.api.event.application.dto.EventDetailInfo
import com.localtalk.api.event.application.mapper.EventApplicationMapper
import com.localtalk.api.event.domain.Event
import com.localtalk.api.event.domain.EventService
import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.domain.Nickname
import com.localtalk.api.review.domain.ReviewService
import com.localtalk.api.visit.domain.VisitService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class EventApplicationServiceTest {
    @MockK
    private lateinit var eventService: EventService

    @MockK
    private lateinit var reviewService: ReviewService

    @MockK
    private lateinit var bookmarkService: BookmarkService

    @MockK
    private lateinit var visitService: VisitService

    @MockK
    private lateinit var eventApplicationMapper: EventApplicationMapper

    @InjectMockKs
    private lateinit var eventApplicationService: EventApplicationService

    @Test
    fun `회원일 경우 북마크와 방문상태를 포함하여 행사 상세정보를 반환한다`() {
        //given
        val eventId = 1L
        val memberId = 100L
        val event = createTestEvent()
        val expectedEventDetailInfo = createTestEventDetailInfo(
            isBookmarked = true,
            isVisited = false,
            isLoggedIn = true
        )

        every { eventService.getEventByIdOrThrow(eventId) } returns event
        every { bookmarkService.isMemberBookmarked(eventId, memberId) } returns true
        every { visitService.isMemberVisited(eventId, memberId) } returns false
        every { reviewService.getTotalReviewCount(eventId) } returns 10
        every { reviewService.getAverageRating(eventId) } returns 4.5
        every {
            eventApplicationMapper.toEventDetailInfo(
                event = event,
                imageUrl = any(),
                isLoggedIn = true,
                isBookmarked = true,
                isVisited = false,
                totalReviewCount = 10,
                averageRating = 4.5
            )
        } returns expectedEventDetailInfo

        //when
        val result = eventApplicationService.getEventDetail(eventId, memberId)

        //then
        assertThat(result).isEqualTo(expectedEventDetailInfo)
        assertThat(result.isLoggedIn).isTrue()
        assertThat(result.isBookmarked).isTrue()
        assertThat(result.isVisited).isFalse()
        verify { eventService.getEventByIdOrThrow(eventId) }
        verify { bookmarkService.isMemberBookmarked(eventId, memberId) }
        verify { visitService.isMemberVisited(eventId, memberId) }
        verify { reviewService.getTotalReviewCount(eventId) }
        verify { reviewService.getAverageRating(eventId) }

    }

    @Test
    fun `비회원일 경우 북마크와 방문상태를 false로 반환한다`() {
        //given
        val eventId = 1L
        val memberId: Long? = null
        val event = createTestEvent()
        val expectedEventDetailInfo = createTestEventDetailInfo(
            isBookmarked = false,
            isVisited = false,
            isLoggedIn = false
        )

        every { eventService.getEventByIdOrThrow(eventId) } returns event
        every { reviewService.getTotalReviewCount(eventId) } returns 10
        every { reviewService.getAverageRating(eventId) } returns 4.5
        every {
            eventApplicationMapper.toEventDetailInfo(
                event = event,
                imageUrl = any(),
                isLoggedIn = false,
                isBookmarked = false,
                isVisited = false,
                totalReviewCount = 10,
                averageRating = 4.5
            )
        } returns expectedEventDetailInfo

        //when
        val result = eventApplicationService.getEventDetail(eventId, memberId)

        //then
        assertThat(result).isEqualTo(expectedEventDetailInfo)
        assertThat(result.isLoggedIn).isFalse()
        assertThat(result.isVisited).isFalse()
        assertThat(result.isBookmarked).isFalse()
        verify { eventService.getEventByIdOrThrow(eventId) }
        verify(exactly = 0) { bookmarkService.isMemberBookmarked(any(), any()) }
        verify(exactly = 0) { visitService.isMemberVisited(any(), any()) }

    }

    private fun createTestEvent(): Event {
        return Event(
            title = "테스트 행사 제목",
            startDate = LocalDate.of(2025,8,29),
            price = 5000,
            member = Member(nickname = Nickname("테스트 유저"))
        )
    }

    private fun createTestEventDetailInfo(isBookmarked: Boolean, isVisited: Boolean, isLoggedIn: Boolean): EventDetailInfo {
        return EventDetailInfo(
            title = "테스트 행사 제목",
            description = null,
            imageUrl = "https://example.com/image.jpg",
            startDate = LocalDate.of(2025,8,29),
            endDate = null,
            operatingHours = null,
            location = null,
            address = null,
            price = 5000,
            contactPhone = null,
            officialWebsite = null,
            latitude = null,
            longitude = null,
            isLoggedIn = isLoggedIn,
            isBookmarked = isBookmarked,
            isVisited = isVisited,
            averageRating = 4.5,
            totalReviewCount = 10
        )
    }
}
