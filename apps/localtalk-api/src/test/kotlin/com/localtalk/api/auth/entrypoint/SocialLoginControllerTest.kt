package com.localtalk.api.auth.entrypoint

import com.localtalk.api.config.KakaoApiMockServer
import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class SocialLoginControllerTest : IntegrationTest() {

    @Test
    fun `카카오 소셜 로그인 성공 시 토큰을 반환한다`() {
        val validAccessToken = "valid_kakao_access_token"

        KakaoApiMockServer.enqueueSuccessResponse(id = 123456789L, expiresIn = 3600, appId = 12345)

        val response = webTestClient
            .post()
            .uri("/api/v1/social-logins/kakao")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"access_token":"$validAccessToken"}""")
            .exchange()

        response
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
            .jsonPath("$.message").isEqualTo("소셜 로그인이 성공했습니다")
            .jsonPath("$.data.access_token").isNotEmpty
            .jsonPath("$.data.refresh_token").isNotEmpty
            .jsonPath("$.data.is_signed_user").isEqualTo(false)
    }

    @Test
    fun `잘못된 카카오 액세스 토큰으로 요청 시 400 에러를 반환한다`() {
        val invalidAccessToken = "invalid_kakao_access_token"

        KakaoApiMockServer.enqueueErrorResponse(code = -401, message = "Invalid token")

        val response = webTestClient
            .post()
            .uri("/api/v1/social-logins/kakao")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"access_token":"$invalidAccessToken"}""")
            .exchange()

        response
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
            .jsonPath("$.message").isEqualTo("카카오 API 오류 - Invalid token (코드: -401)")
    }

    @Test
    fun `지원하지 않는 소셜 로그인 프로바이더로 요청 시 400 에러를 반환한다`() {
        val accessToken = "some_token"
        val provider = "invalid_provider"

        val response = webTestClient
            .post()
            .uri("/api/v1/social-logins/{provider}", provider)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"access_token":"$accessToken"}""")
            .exchange()

        response
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
            .jsonPath("$.message").isEqualTo("지원하지 않는 소셜 로그인 프로바이더입니다: $provider")
    }

    @Test
    fun `액세스 토큰이 null인 경우 400 에러를 반환한다`() {
        val requestBody = """{"access_token": null}"""

        val response = webTestClient
            .post()
            .uri("/api/v1/social-logins/kakao")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()

        response
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
            .jsonPath("$.message").isEqualTo("액세스 토큰을 입력해주세요.")
    }

    @Test
    fun `액세스 토큰이 빈 문자열인 경우 400 에러를 반환한다`() {
        val response = webTestClient
            .post()
            .uri("/api/v1/social-logins/kakao")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"access_token":""}""")
            .exchange()

        response
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
            .jsonPath("$.message").isEqualTo("액세스 토큰은 빈 값일 수 없습니다.")
    }

}
