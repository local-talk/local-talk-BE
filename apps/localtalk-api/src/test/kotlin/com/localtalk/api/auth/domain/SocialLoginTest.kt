package com.localtalk.api.auth.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class SocialLoginTest {

    @Nested
    inner class `소셜 사용자로 회원 가입여부를 확인할 때` {

        @Test
        fun `가입된 회원이라면 true를 반환한다`() {
            val socialLoginUser = SocialLogin(
                provider = SocialLoginProvider.KAKAO,
                socialKey = "socialKey",
                memberId = 1L,
            )

            val got = socialLoginUser.isSignedUser()

            assertThat(got).isTrue
        }

        @Test
        fun `가입되지 않은 회원이라면 false를 반환한다`() {
            val socialLoginUser = SocialLogin(
                provider = SocialLoginProvider.KAKAO,
                socialKey = "socialKey",
                memberId = null,
            )

            val got = socialLoginUser.isSignedUser()

            assertThat(got).isFalse
        }
    }
}
