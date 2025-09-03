package com.localtalk.api.event.application

import com.localtalk.api.bookmark.domain.BookmarkService
import com.localtalk.api.event.application.mapper.EventApplicationMapper
import com.localtalk.api.event.domain.EventService
import com.localtalk.api.event.fixture.EventDetailInfoFixture
import com.localtalk.api.event.fixture.EventFixture
import com.localtalk.api.review.domain.ReviewService
import com.localtalk.api.visit.domain.VisitService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
        val event = EventFixture.createEvent()
        val expectedEventDetailInfo = EventDetailInfoFixture.createEventDetailInfo(
            isBookmarked = true,
            isVisited = false,
            isLoggedIn = true
        )

        every { eventService.getEventByIdOrThrow(eventId) } returns event
        every { eventService.getImageUrl(event) } returns "https://localtalk-storage.s3.ap-northeast-2.amazonaws.com/test/event-image.jpg"
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
        assertThat(result.totalReviewCount).isEqualTo(10)
        assertThat(result.averageRating).isEqualTo(4.5)
    }

    @Test
    fun `비회원일 경우 북마크와 방문상태를 false로 반환한다`() {
        //given
        val eventId = 1L
        val memberId: Long? = null
        val event = EventFixture.createEvent()
        val expectedEventDetailInfo = EventDetailInfoFixture.createEventDetailInfo(
            isBookmarked = false,
            isVisited = false,
            isLoggedIn = false
        )

        every { eventService.getEventByIdOrThrow(eventId) } returns event
        every { eventService.getImageUrl(event) } returns "https://localtalk-storage.s3.ap-northeast-2.amazonaws.com/test/event-image.jpg"
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
        assertThat(result.totalReviewCount).isEqualTo(10)
        assertThat(result.averageRating).isEqualTo(4.5)
    }
}
