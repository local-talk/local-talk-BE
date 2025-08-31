package com.localtalk.api.review.domain

import com.localtalk.api.event.domain.Event
import com.localtalk.api.event.domain.EventRepository
import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.domain.MemberRepository
import com.localtalk.api.member.domain.Nickname
import com.localtalk.api.support.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class ReviewRepositoryTest : IntegrationTest() {
    @Autowired
    private lateinit var reviewRepository: ReviewRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var reviewer: Member
    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        val eventCreator = memberRepository.save(
            Member(nickname = Nickname("행사 창시자"))
        )

        event = eventRepository.save(
            Event(
                title = "테스트 행사 제목",
                startDate = LocalDate.of(2025, 8, 29),
                price = 5000,
                member = eventCreator
            )
        )

        reviewer = memberRepository.save(
            Member(nickname = Nickname("리뷰어"))
        )
    }

    @Nested
    inner class `countByEventIdAndDeletedAtIsNull 메서드는` {

        @Test
        fun `행사의 리뷰 개수를 정확히 반환한다`() {
            // given
            reviewRepository.save(Review(event = event, member = reviewer, rating = 5))
            reviewRepository.save(Review(event = event, member = reviewer, rating = 4))

            // when
            val result = reviewRepository.countByEventIdAndDeletedAtIsNull(event.id)

            // then
            assertThat(result).isEqualTo(2)
        }

        @Test
        fun `리뷰가 없는 행사인 경우 0을 반환한다`() {
            // when
            val result = reviewRepository.countByEventIdAndDeletedAtIsNull(event.id)

            // then
            assertThat(result).isEqualTo(0)
        }

        @Test
        fun `삭제된 리뷰는 개수에 포함되지 않는다`() {
            // given
            reviewRepository.save(Review(event = event, member = reviewer, rating = 5))
            val deletedReview = reviewRepository.save(Review(event = event, member = reviewer, rating = 4))
            deletedReview.delete()
            reviewRepository.save(deletedReview)

            // when
            val result = reviewRepository.countByEventIdAndDeletedAtIsNull(event.id)

            // then
            assertThat(result).isEqualTo(1)
        }
    }

    @Nested
    inner class `getAverageRatingByEventId 메서드는` {

        @Test
        fun `행사의 평균 평점을 정확히 계산하여 반환한다`() {
            // given
            reviewRepository.save(Review(event = event, member = reviewer, rating = 5))
            reviewRepository.save(Review(event = event, member = reviewer, rating = 3))
            reviewRepository.save(Review(event = event, member = reviewer, rating = 4))

            // when
            val result = reviewRepository.getAverageRatingByEventId(event.id)

            // then
            assertThat(result).isEqualTo(4.0)
        }

        @Test
        fun `리뷰가 없는 행사인 경우 0을 반환한다`() {
            // when
            val result = reviewRepository.getAverageRatingByEventId(event.id)

            // then
            assertThat(result).isNull()
        }

        @Test
        fun `삭제된 리뷰는 평균 계산에 포함되지 않는다`() {
            // given
            reviewRepository.save(Review(event = event, member = reviewer, rating = 5))
            val deletedReview = reviewRepository.save(Review(event = event, member = reviewer, rating = 1))
            deletedReview.delete()
            reviewRepository.save(deletedReview)

            // when
            val result = reviewRepository.getAverageRatingByEventId(event.id)

            // then
            assertThat(result).isEqualTo(5.0)
        }
    }
}
