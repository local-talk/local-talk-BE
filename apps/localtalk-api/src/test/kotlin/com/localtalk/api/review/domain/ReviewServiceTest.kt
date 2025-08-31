package com.localtalk.api.review.domain

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ReviewServiceTest {

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @InjectMockKs
    private lateinit var reviewService: ReviewService

    private val eventId = 1L

    @Nested
    inner class `getTotalReviewCount 메서드는` {

        @Test
        fun `리뷰가 존재하면 정확한 개수를 반환한다`() {
            // given
            every { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) } returns 5

            // when
            val result = reviewService.getTotalReviewCount(eventId)

            // then
            assertThat(result).isEqualTo(5)
            verify { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) }
        }

        @Test
        fun `리뷰가 없으면 0을 반환한다`() {
            // given
            every { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) } returns 0

            // when
            val result = reviewService.getTotalReviewCount(eventId)

            // then
            assertThat(result).isEqualTo(0)
            verify { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) }
        }
    }

    @Nested
    inner class `getAverageRating 메서드는` {

        @Test
        fun `리뷰가 존재하면 평균 평점을 반환한다`() {
            // given
            every { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) } returns 3
            every { reviewRepository.getAverageRatingByEventId(eventId) } returns 4.0

            // when
            val result = reviewService.getAverageRating(eventId)

            // then
            assertThat(result).isEqualTo(4.0)
            verify { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) }
            verify { reviewRepository.getAverageRatingByEventId(eventId) }
        }

        @Test
        fun `리뷰가 없으면 0_0을 반환한다`() {
            // given
            every { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) } returns 0

            // when
            val result = reviewService.getAverageRating(eventId)

            // then
            assertThat(result).isEqualTo(0.0)
            verify { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) }
        }

        @Test
        fun `get average rating by event id 가 null 을 반환하면 0_0을 반환한다`() {
            // given
            every { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) } returns 2
            every { reviewRepository.getAverageRatingByEventId(eventId) } returns null

            // when
            val result = reviewService.getAverageRating(eventId)

            // then
            assertThat(result).isEqualTo(0.0)
            verify { reviewRepository.countByEventIdAndDeletedAtIsNull(eventId) }
            verify { reviewRepository.getAverageRatingByEventId(eventId) }
        }
    }
}
