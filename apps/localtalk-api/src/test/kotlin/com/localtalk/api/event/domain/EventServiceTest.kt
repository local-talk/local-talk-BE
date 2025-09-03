package com.localtalk.api.event.domain

import com.localtalk.api.event.fixture.EventFixture
import com.localtalk.api.infrastructure.storage.S3ImageUrlGenerator
import com.localtalk.s3.config.S3Properties
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class EventServiceTest {

    @MockK
    private lateinit var eventRepository: EventRepository

    @MockK
    private lateinit var s3ImageUrlGenerator: S3ImageUrlGenerator

    @InjectMockKs
    private lateinit var eventService: EventService

    @Nested
    inner class `getEventByIdOrThrow 메서드는` {
        @Test
        fun `존재하는 행사 ID로 조회 시 행사를 반환한다`() {
            // given
            val eventId = 1L
            val expectedEvent = EventFixture.createEvent(title = "테스트 행사")
            every { eventRepository.findByIdAndDeletedAtIsNull(eventId) } returns expectedEvent

            // when
            val result = eventService.getEventByIdOrThrow(eventId)

            // then
            assertThat(result).isEqualTo(expectedEvent)
        }

        @Test
        fun `존재하지 않는 행사 ID인 경우 예외를 발생시킨다`() {
            // given
            val eventId = 999L
            every { eventRepository.findByIdAndDeletedAtIsNull(eventId) } returns null

            // when & then
            assertThatThrownBy { eventService.getEventByIdOrThrow(eventId) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("존재하지 않는 행사입니다.")
        }
    }
}
