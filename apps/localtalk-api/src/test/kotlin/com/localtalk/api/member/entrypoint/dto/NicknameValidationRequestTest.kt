package com.localtalk.api.member.entrypoint.dto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NicknameValidationRequestTest {

    @Nested
    inner class `유효한 요청 생성` {

        @Test
        fun `유효한 닉네임이면 요청 객체가 성공적으로 생성된다`() {
            val validNickname = "홍길동"

            val got = NicknameValidationRequest(nickname = validNickname)

            assertThat(got.nickname).isEqualTo(validNickname)
        }
    }

    @Nested
    inner class `잘못된 요청 검증` {

        @Test
        fun `닉네임이 null이면 IllegalArgumentException을 발생시킨다`() {
            assertThatThrownBy { NicknameValidationRequest(nickname = null) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("닉네임은 필수입니다")
        }
    }
}
