package com.localtalk.api.auth.application.dto

import com.localtalk.api.auth.domain.SocialLoginProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class SocialLoginCommandTest {

    @Nested
    inner class `소셜 로그인 커맨드 생성시` {
        @Test
        fun `유효한 인자가 주어지면 객체를 생성한다`() {
            val provider = SocialLoginProvider.KAKAO
            val accessToken = "valid-access-token"

            val got = SocialLoginCommand(provider, accessToken)

            assertThat(got.provider).isEqualTo(provider)
            assertThat(got.accessToken).isEqualTo(accessToken)
        }

        @Test
        fun `액세스 토큰이 비어있으면 IllegalArgumentException을 던진다`() {
            val provider = SocialLoginProvider.KAKAO
            val accessToken = ""

            assertThatThrownBy { SocialLoginCommand(provider, accessToken) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("액세스 토큰은 빈 값일 수 없습니다.")
        }
    }

}
