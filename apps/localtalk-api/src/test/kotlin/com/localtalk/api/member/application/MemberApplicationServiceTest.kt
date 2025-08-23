package com.localtalk.api.member.application

import com.localtalk.api.member.domain.MemberService
import com.localtalk.api.member.domain.Nickname
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MemberApplicationServiceTest {

    @MockK
    private lateinit var memberService: MemberService

    @InjectMockKs
    private lateinit var memberApplicationService: MemberApplicationService

    @Nested
    inner class `닉네임 사용 가능 여부 검증` {

        @Test
        fun `사용 가능한 닉네임이면 회원가입에 사용할 수 있다`() {
            val nickname = Nickname("새로운닉네임")
            every { memberService.isNicknameExists(nickname) } returns false

            memberApplicationService.validateNickname(nickname)

            verify { memberService.isNicknameExists(nickname) }
        }

        @Test
        fun `이미 다른 회원이 사용 중인 닉네임이면 IllegalArgumentException을 발생시킨다`() {
            val nickname = Nickname("기존회원닉네임")
            every { memberService.isNicknameExists(nickname) } returns true

            assertThatThrownBy { memberApplicationService.validateNickname(nickname) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("이미 사용 중인 닉네임입니다")

            verify { memberService.isNicknameExists(nickname) }
        }
    }
}
