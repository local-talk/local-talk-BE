package com.localtalk.api.member.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProfileUrlTest {

    @Nested
    inner class `ProfileUrl을 생성할 때` {

        @Test
        fun `유효한 HTTPS URL로 생성한다`() {
            val validUrl = "https://example.com/profile.jpg"

            val want = validUrl
            val got = ProfileUrl(validUrl).value

            assertThat(got).isEqualTo(want)
        }

        @Test
        fun `유효한 HTTP URL로 생성한다`() {
            val validUrl = "http://example.com/profile.jpg"

            val want = validUrl
            val got = ProfileUrl(validUrl).value

            assertThat(got).isEqualTo(want)
        }

        @Test
        fun `복잡한 URL 파라미터가 있어도 생성한다`() {
            val validUrl = "https://example.com/profile.jpg?size=large&format=webp"

            val want = validUrl
            val got = ProfileUrl(validUrl).value

            assertThat(got).isEqualTo(want)
        }

        @Test
        fun `빈 문자열이면 예외를 발생시킨다`() {
            assertThatThrownBy { ProfileUrl("") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("프로필 URL은 비어있을 수 없습니다")
        }

        @Test
        fun `공백 문자열이면 예외를 발생시킨다`() {
            assertThatThrownBy { ProfileUrl("   ") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("프로필 URL은 비어있을 수 없습니다")
        }

        @Test
        fun `HTTP나 HTTPS가 없으면 예외를 발생시킨다`() {
            assertThatThrownBy { ProfileUrl("example.com/profile.jpg") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("올바른 프로필 URL 형식이 아닙니다")
        }

        @Test
        fun `잘못된 URL 형식이면 예외를 발생시킨다`() {
            assertThatThrownBy { ProfileUrl("not-a-url") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("올바른 프로필 URL 형식이 아닙니다")
        }

        @Test
        fun `특수문자가 포함된 잘못된 형식이면 예외를 발생시킨다`() {
            assertThatThrownBy { ProfileUrl("http://invalid url with spaces") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("올바른 프로필 URL 형식이 아닙니다")
        }
    }
}