package com.localtalk.infrastructure.http.formatter

import com.fasterxml.jackson.databind.ObjectMapper
import com.localtalk.infrastructure.http.model.CachedBodyRequestWrapper
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.util.ContentCachingResponseWrapper

class HttpLogFormatterTest {

    private val objectMapper = ObjectMapper()
    private val httpLogFormatter = HttpLogFormatter(objectMapper)

    @Nested
    @DisplayName("formatRequest")
    inner class FormatRequestTest {

        @Test
        @DisplayName("본문이 있는 요청이면 본문을 포함하여 포맷합니다.")
        fun shouldFormatRequestWithBody() {
            val request = mockk<CachedBodyRequestWrapper>()
            every { request.method } returns "POST"
            every { request.requestURI } returns "/api/users"
            every { request.queryString } returns "page=1"
            every { request.hasBody() } returns true
            every { request.getTruncatedBody() } returns """{"name":"홍길동"}"""

            val result = httpLogFormatter.formatRequest(request)

            assertThat(result).contains("📥 [HTTP REQUEST]")
            assertThat(result).contains("Method    : POST")
            assertThat(result).contains("URL       : /api/users?page=1")
            assertThat(result).contains("\"name\" : \"홍길동\"")
        }

        @Test
        @DisplayName("본문이 없는 요청이면 본문 없이 포맷합니다.")
        fun shouldFormatRequestWithoutBody() {
            val request = mockk<CachedBodyRequestWrapper>()
            every { request.method } returns "GET"
            every { request.requestURI } returns "/api/users"
            every { request.queryString } returns null
            every { request.hasBody() } returns false

            val result = httpLogFormatter.formatRequest(request)

            assertThat(result).contains("📥 [HTTP REQUEST]")
            assertThat(result).contains("Method    : GET")
            assertThat(result).contains("URL       : /api/users")
            assertThat(result).doesNotContain("\"")
        }
    }

    @Nested
    @DisplayName("formatResponse")
    inner class FormatResponseTest {

        @Test
        @DisplayName("본문이 있는 응답이면 본문을 포함하여 포맷합니다.")
        fun shouldFormatResponseWithBody() {
            val response = mockk<ContentCachingResponseWrapper>()
            every { response.status } returns 200
            every { response.contentAsByteArray } returns """{"id":1}""".toByteArray()

            val result = httpLogFormatter.formatResponse(response, 120L)

            assertThat(result).contains("📤 [HTTP RESPONSE]")
            assertThat(result).contains("Status    : 200")
            assertThat(result).contains("Duration  : 120ms")
            assertThat(result).contains("\"id\" : 1")
        }

        @Test
        @DisplayName("본문이 없는 응답이면 본문 없이 포맷합니다.")
        fun shouldFormatResponseWithoutBody() {
            val response = mockk<ContentCachingResponseWrapper>()
            every { response.status } returns 204
            every { response.contentAsByteArray } returns ByteArray(0)

            val result = httpLogFormatter.formatResponse(response, 50L)

            assertThat(result).contains("📤 [HTTP RESPONSE]")
            assertThat(result).contains("Status    : 204")
            assertThat(result).contains("Duration  : 50ms")
            assertThat(result).doesNotContain("\"")
        }
    }
}