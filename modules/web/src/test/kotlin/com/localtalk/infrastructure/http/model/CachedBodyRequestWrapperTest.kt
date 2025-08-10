package com.localtalk.infrastructure.http.model

import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayInputStream

class CachedBodyRequestWrapperTest {

    @Nested
    @DisplayName("hasBody")
    inner class HasBodyTest {

        @Test
        @DisplayName("본문이 있으면 true를 반환합니다.")
        fun shouldReturnTrueWhenBodyExists() {
            val request = mockk<HttpServletRequest>()
            every { request.inputStream } returns ByteArrayInputStream("본문 내용".toByteArray()).asServletInputStream()

            val wrapper = CachedBodyRequestWrapper(request)

            assertThat(wrapper.hasBody()).isTrue
        }

        @Test
        @DisplayName("본문이 없으면 false를 반환합니다.")
        fun shouldReturnFalseWhenBodyIsEmpty() {
            val request = mockk<HttpServletRequest>()
            every { request.inputStream } returns ByteArrayInputStream(ByteArray(0)).asServletInputStream()

            val wrapper = CachedBodyRequestWrapper(request)

            assertThat(wrapper.hasBody()).isFalse
        }
    }

    @Nested
    @DisplayName("getTruncatedBody")
    inner class GetTruncatedBodyTest {

        @ParameterizedTest
        @DisplayName("본문 길이가 전달한 값보다 작거나 같으면 본문을 그대로 반환합니다.")
        @ValueSource(strings = ["123", "12345"])
        fun shouldReturnFullBodyWhenLengthLessOrEqual(body: String) {
            val request = mockk<HttpServletRequest>()
            every { request.inputStream } returns ByteArrayInputStream(body.toByteArray()).asServletInputStream()

            val wrapper = CachedBodyRequestWrapper(request)
            val result = wrapper.getTruncatedBody(5)

            assertThat(result).isEqualTo(body)
        }

        @Test
        @DisplayName("본문 길이가 전달한 값보다 길면 본문을 자른 후 몇자인지 표시합니다.")
        fun shouldTruncateWhenLengthGreaterThanMax() {
            val request = mockk<HttpServletRequest>()
            every { request.inputStream } returns ByteArrayInputStream("1234567".toByteArray()).asServletInputStream()

            val wrapper = CachedBodyRequestWrapper(request)
            val result = wrapper.getTruncatedBody(5)

            assertThat(result).isEqualTo("12345... (7 chars)")
        }
    }

    private fun ByteArrayInputStream.asServletInputStream() = object : jakarta.servlet.ServletInputStream() {
        override fun read(): Int = this@asServletInputStream.read()
        override fun isFinished(): Boolean = available() == 0
        override fun isReady(): Boolean = true
        override fun setReadListener(readListener: jakarta.servlet.ReadListener?) {}
    }
}
