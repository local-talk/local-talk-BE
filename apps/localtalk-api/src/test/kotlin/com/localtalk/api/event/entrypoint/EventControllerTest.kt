package com.localtalk.api.event.entrypoint

import com.localtalk.api.event.domain.Event
import com.localtalk.api.event.domain.EventRepository
import com.localtalk.api.event.fixture.EventFixture
import com.localtalk.api.member.domain.MemberRepository
import com.localtalk.api.member.fixture.MemberFixture
import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.LocalDate

class EventControllerTest : IntegrationTest() {

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        val member = memberRepository.save(MemberFixture.createMember())
        event = eventRepository.save(
            EventFixture.createEvent(
                title = "테스트 행사 제목",
                description = "테스트 행사 설명",
                startDate = LocalDate.of(2025, 7, 29),
                member = member
            )
        )
    }

    @Nested
    inner class `행사 상세정보 조회` {

        @Test
        fun `비회원이 행사 상세정보 조회를 성공한다`() {
            val response = webTestClient.get()
                .uri("/api/v1/events/{eventId}", event.id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            response
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.data.title").isEqualTo("테스트 행사 제목")
                .jsonPath("$.data.description").isEqualTo("테스트 행사 설명")
                .jsonPath("$.data.image_url").isEqualTo("https://localtalk-storage.s3.us-east-1.amazonaws.com/test/event-image.jpg")
                .jsonPath("$.data.start_date").isEqualTo(LocalDate.of(2025, 7, 29))
                .jsonPath("$.data.is_logged_in").isEqualTo(false)
                .jsonPath("$.data.is_bookmarked").isEqualTo(false)
                .jsonPath("$.data.is_visited").isEqualTo(false)
                .jsonPath("$.data.price").isEqualTo(5000)
                .jsonPath("$.message").isEqualTo("행사 상세 정보를 성공적으로 조회했습니다")

        }


        @Test
        fun `존재하지 않는 행사 ID로 조회 시 예외가 발생한다`() {
            val response = webTestClient.get()
                .uri("/api/v1/events/{eventId}", 999L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("존재하지 않는 행사입니다.")
        }

        @Test
        fun `삭제된 행사 조회 시 예외가 발생한다`() {
            event.delete()
            eventRepository.save(event)

            val response = webTestClient.get()
                .uri("/api/v1/events/{eventId}", event.id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("존재하지 않는 행사입니다.")
        }
    }
}
