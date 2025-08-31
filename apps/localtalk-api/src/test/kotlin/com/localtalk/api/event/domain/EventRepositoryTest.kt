package com.localtalk.api.event.domain

import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.domain.MemberRepository
import com.localtalk.api.member.domain.Nickname
import com.localtalk.api.support.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class EventRepositoryTest : IntegrationTest() {
    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository


    @Nested
    inner class `findByIdActive 메서드는` {

        @Test
        fun `삭제되지 않은 행사를 정상 조회한다`() {
            //given
            val event = saveEvent()

            //when
            val result = eventRepository.findByIdActive(event.id)

            //then
            assertThat(result).isNotNull
            assertThat(result?.title).isEqualTo("테스트 행사 제목")
        }

        @Test
        fun `삭제된 이벤트는 조회되지 않는다`() {
            //given
            val event = saveEvent()
            event.delete()
            eventRepository.save(event)

            //when
            val result = eventRepository.findByIdActive(event.id)

            //then
            assertThat(result).isNull()
        }

        @Test
        fun `존재하지 않는 ID는 NULL을 반환한다`() {
            //when
            val result = eventRepository.findByIdActive(999L)

            //then
            assertThat(result).isNull()
        }
    }

    private fun saveEvent(): Event {
        return eventRepository.save(
            Event(
                title = "테스트 행사 제목",
                startDate = LocalDate.of(2025,8,29),
                price = 5000,
                member = memberRepository.save(
                    Member(nickname = Nickname("테스트 유저"))
                )
            )
        )
    }
}
