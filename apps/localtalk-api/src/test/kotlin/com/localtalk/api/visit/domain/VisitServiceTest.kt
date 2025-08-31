package com.localtalk.api.visit.domain

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
class VisitServiceTest {

    @MockK
    private lateinit var visitRepository: VisitRepository

    @InjectMockKs
    private lateinit var visitService: VisitService

    @Nested
    inner class `isMemberVisited 메서드는` {

        @Test
        fun `방문 기록이 있으면 true를 반환한다`() {
            // given
            val eventId = 1L
            val memberId = 100L
            every { visitRepository.existsByEventIdAndMemberId(eventId, memberId) } returns true

            // when
            val result = visitService.isMemberVisited(eventId, memberId)

            // then
            assertThat(result).isTrue
            verify { visitRepository.existsByEventIdAndMemberId(eventId, memberId) }
        }

        @Test
        fun `방문 기록이 없으면 false를 반환한다`() {
            // given
            val eventId = 1L
            val memberId = 100L
            every { visitRepository.existsByEventIdAndMemberId(eventId, memberId) } returns false

            // when
            val result = visitService.isMemberVisited(eventId, memberId)

            // then
            assertThat(result).isFalse
            verify { visitRepository.existsByEventIdAndMemberId(eventId, memberId) }
        }
    }
}
