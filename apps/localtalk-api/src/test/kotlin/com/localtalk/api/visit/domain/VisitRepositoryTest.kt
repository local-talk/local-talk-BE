package com.localtalk.api.visit.domain

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

class VisitRepositoryTest : IntegrationTest() {

    @Autowired
    private lateinit var visitRepository: VisitRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: Member
    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        member = memberRepository.save(Member(nickname = Nickname("방문자")))

        val eventCreator = memberRepository.save(Member(nickname = Nickname("행사 창시자")))

        event = eventRepository.save(
            Event(
                title = "테스트 행사 제목",
                startDate = LocalDate.of(2025,8,29),
                price = 5000,
                member = eventCreator
            )
        )
    }

    @Nested
    inner class `existsByEventIdAndMemberId 메서드는` {

        @Test
        fun `방문 기록이 있으면 true를 반환한다`() {
            // given
            visitRepository.save(Visit(event = event, member = member))

            // when
            val exists = visitRepository.existsByEventIdAndMemberId(event.id, member.id)

            // then
            assertThat(exists).isTrue
        }

        @Test
        fun `방문 기록이 없으면 false를 반환한다`() {
            // when
            val result = visitRepository.existsByEventIdAndMemberId(event.id, member.id)

            // then
            assertThat(result).isFalse
        }
    }
}
