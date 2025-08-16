package com.localtalk.api.auth.entrypoint.dto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SocialLoginRequestTest {

    @Nested
    inner class `소셜 로그인 요청 생성시` {

        @Test
        fun `유효한 액세스 토큰이 주어지면 객체를 생성한다`() {
            val want = "valid-access-token"

            val got = SocialLoginRequest(want)

            assertThat(got.accessToken).isEqualTo(want)
        }

        @Test
        fun `null 액세스 토큰이 주어지면 예외를 던진다`() {
            assertThatThrownBy { SocialLoginRequest(null) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("액세스 토큰을 입력해주세요.")
        }
    }
}
