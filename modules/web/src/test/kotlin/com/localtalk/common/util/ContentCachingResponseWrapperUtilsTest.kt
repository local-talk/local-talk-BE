package com.localtalk.common.util

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.web.util.ContentCachingResponseWrapper

class ContentCachingResponseWrapperUtilsTest {

    @Nested
    inner class HasBodyTest {

        @Test
        @DisplayName("응답 본문이 존재하면 true를 반환합니다.")
        fun shouldReturnTrueWhenBodyExists() {
            val wrapper = mockk<ContentCachingResponseWrapper>()
            every { wrapper.contentAsByteArray } returns "응답 본문".toByteArray()

            val result = wrapper.hasBody()

            assertThat(result).isTrue
        }

        @Test
        @DisplayName("응답 본문이 없는 경우 false를 반환합니다.")
        fun shouldReturnFalseWhenBodyIsEmpty() {
            // given
            val wrapper = mockk<ContentCachingResponseWrapper>()
            every { wrapper.contentAsByteArray } returns ByteArray(0)

            // when
            val result = wrapper.hasBody()

            // then
            assertThat(result).isFalse
        }
    }

    @Nested
    @DisplayName("getTruncatedBody")
    inner class GetTruncatedBodyTest {

        @ParameterizedTest
        @DisplayName("응답 본문 길이가 전달한 값 보다 작거나 같으면 본문을 그대로 반환합니다.")
        @ValueSource(strings = ["123", "12345"])
        fun shouldReturnFullBodyWhenLengthLessOrEqual(body: String) {
            val wrapper = mockk<ContentCachingResponseWrapper>()
            every { wrapper.contentAsByteArray } returns body.toByteArray()

            val result = wrapper.getTruncatedBody(5)

            assertThat(result).isEqualTo(body)
        }

        @Test
        @DisplayName("응답 본문 길이가 전달한 값보다 길면 본문을 자른 후 몇자인지 표시합니다.")
        fun shouldTruncateWhenLengthGreaterThanMax() {
            // given
            val wrapper = mockk<ContentCachingResponseWrapper>()
            every { wrapper.contentAsByteArray } returns "1234567".toByteArray()

            // when
            val result = wrapper.getTruncatedBody(5)

            // then
            assertThat(result).isEqualTo("12345... (7 chars)")
        }
    }
}