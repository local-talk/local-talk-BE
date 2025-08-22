package com.localtalk.api.common.exception

import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test-rest-api-exceptions")
class RestApiTestController {

    @GetMapping("/get-only")
    fun getOnlyEndpoint(): String = "GET only"

    @PostMapping("/post-only", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postOnlyEndpoint(@RequestBody request: TestRequest): String = "POST only"
}

@RestController
@RequestMapping("/test-json-exceptions")
class JsonTestController {

    @PostMapping("/validation-error")
    fun validationErrorEndpoint(@RequestBody request: TestRequest): String = "success"
}

@RestController
@RequestMapping("/test-argument-exceptions")
class ArgumentTestController {

    @GetMapping("/illegal-argument")
    fun throwIllegalArgumentException(): String {
        throw IllegalArgumentException("테스트용 IllegalArgumentException")
    }
}

@RestController
@RequestMapping("/test-generic-exceptions")
class GenericTestController {

    @GetMapping("/runtime-exception")
    fun throwRuntimeException(): String {
        throw RuntimeException("테스트용 RuntimeException")
    }
}

data class TestRequest(
    val validField: String?,
) {
    init {
        require(!validField.isNullOrBlank()) { "validField는 필수이며 빈 값일 수 없습니다" }
    }
}


@DisplayName("GlobalExceptionHandler 테스트")
class GlobalExceptionHandlerTest : IntegrationTest() {
    @Nested
    @DisplayName("REST API 예외 처리")
    inner class RestApiExceptionHandling {

        @Test
        fun `지원하지 않는 HTTP 메서드 요청시 400 상태코드를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.post()
                .uri("/test-rest-api-exceptions/get-only")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isNotEmpty
                .jsonPath("$.data").isEmpty
        }

        @Test
        fun `잘못된 Content-Type으로 요청시 400 상태코드를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.post()
                .uri("/test-rest-api-exceptions/post-only")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("plain text")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isNotEmpty
                .jsonPath("$.data").isEmpty
        }
    }

    @Nested
    @DisplayName("HttpMessageNotReadableException 처리")
    inner class HttpMessageNotReadableExceptionHandling {

        @Test
        fun `잘못된 JSON 형식 요청시 400 상태코드와 형식 오류 메시지를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.post()
                .uri("/test-json-exceptions/validation-error")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{invalid json}")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("잘못된 요청 형식입니다.")
                .jsonPath("$.data").isEmpty
        }

        @Test
        fun `JSON 파싱 중 IllegalArgumentException 발생시 해당 예외 메시지를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.post()
                .uri("/test-json-exceptions/validation-error")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"validField": ""}""")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("validField는 필수이며 빈 값일 수 없습니다")
                .jsonPath("$.data").isEmpty
        }

        @Test
        fun `JSON 파싱 중 IllegalArgumentException 발생시 null인 경우 기본 메시지를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.post()
                .uri("/test-json-exceptions/validation-error")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"validField": null}""")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("validField는 필수이며 빈 값일 수 없습니다")
                .jsonPath("$.data").isEmpty
        }
    }

    @Nested
    @DisplayName("IllegalArgumentException 처리")
    inner class IllegalArgumentExceptionHandling {

        @Test
        fun `직접 던진 IllegalArgumentException 발생시 400 상태코드와 예외 메시지를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.get()
                .uri("/test-argument-exceptions/illegal-argument")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("테스트용 IllegalArgumentException")
                .jsonPath("$.data").isEmpty
        }
    }

    @Nested
    @DisplayName("일반 예외 처리")
    inner class GenericExceptionHandling {

        @Test
        fun `Exception 발생시 500 상태코드와 서버 오류 메시지를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.get()
                .uri("/test-generic-exceptions/runtime-exception")
                .exchange()
                .expectStatus().is5xxServerError
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .jsonPath("$.message").isEqualTo("서버 내부 오류가 발생했습니다.")
                .jsonPath("$.data").isEmpty
        }

        @Test
        fun `존재하지 않는 엔드포인트 요청시 404 상태코드를 반환한다`() {
            val client = loginAsTemporaryMember()

            client.get()
                .uri("/non-existent-endpoint")
                .exchange()
                .expectStatus().isNotFound
        }
    }
}
