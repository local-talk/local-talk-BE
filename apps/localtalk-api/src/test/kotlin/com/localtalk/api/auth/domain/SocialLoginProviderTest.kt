package com.localtalk.api.auth.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SocialLoginProviderTest {

    @Nested
    inner class `소셜 로그인 프로바이더 이름이 문자열로 주어지면` {

        @ParameterizedTest
        @ValueSource(strings = ["kakao", "KAKAO", "Kakao", "KaKaO"])
        fun `카카오 프로바이더에 해당하는 경우 KAKAO로 변환한다`(input: String) {
            val want = SocialLoginProvider.KAKAO

            val got = SocialLoginProvider.fromString(input)

            assertThat(got).isEqualTo(want)
            assertThat(got.description).isEqualTo("카카오")
        }

        @ParameterizedTest
        @ValueSource(strings = ["apple", "APPLE", "Apple", "ApPlE"])
        fun `애플 프로바이더에 해당하는 경우 APPLE로 변환한다`(input: String) {
            val want = SocialLoginProvider.APPLE

            val got = SocialLoginProvider.fromString(input)

            assertThat(got).isEqualTo(want)
            assertThat(got.description).isEqualTo("애플")
        }

        @ParameterizedTest
        @ValueSource(strings = ["google", "facebook", "naver", "unknown", ""])
        fun `지원하지 않는 프로바이더 이름인 경우 IllegalArgumentException을 던진다`(input: String) {
            assertThatThrownBy { SocialLoginProvider.fromString(input) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("지원하지 않는 소셜 로그인 프로바이더입니다: $input")
        }

        @Test
        fun `공백이 포함된 프로바이더 이름은 예외를 던진다`() {
            assertThatThrownBy { SocialLoginProvider.fromString(" kakao ") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("지원하지 않는 소셜 로그인 프로바이더입니다:  kakao ")
        }

    }

}
