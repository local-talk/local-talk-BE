package com.localtalk.api.auth.entrypoint

import com.fasterxml.jackson.databind.JsonNode
import com.localtalk.api.auth.infrastructure.token.JwtProperties
import com.localtalk.api.support.IntegrationTest
import com.localtalk.api.support.KakaoApiMockServer
import com.localtalk.api.utils.JwtTestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.returnResult

class AuthControllerTest : IntegrationTest() {

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @Nested
    inner class `토큰 갱신 API를 호출할 때` {

        @Test
        fun `유효한 리프레시 토큰을 전달하면 토큰을 재발급 받는다`() {
            KakaoApiMockServer.enqueueSuccessResponse()

            val loginResponse = webTestClient.post()
                .uri("/api/v1/social-logins/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"access_token":"valid_kakao_access_token"}""")
                .exchange()
                .expectStatus().isOk
                .returnResult<JsonNode>()
                .responseBody
                .blockFirst()!!

            val accessToken = loginResponse["data"]["access_token"].asText()
            val refreshToken = loginResponse["data"]["refresh_token"].asText()

            val response = webTestClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                .build()
                .post()
                .uri("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"refresh_token":"$refreshToken"}""")
                .exchange()
                .expectStatus().isOk
                .returnResult<JsonNode>()
                .responseBody
                .blockFirst()!!

            assertThat(response["code"].asInt()).isEqualTo(200)
            assertThat(response["message"].asText()).isEqualTo("토큰이 성공적으로 갱신되었습니다")

            val newAccessToken = response["data"]["access_token"].asText()
            val newRefreshToken = response["data"]["refresh_token"].asText()

            assertThat(JwtTestUtils.isValidJwtFormat(newAccessToken)).isTrue()
            assertThat(JwtTestUtils.isValidJwtFormat(newRefreshToken)).isTrue()
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                """{}""",
                """{"refresh_token":null}""",
            ],
        )
        fun `잘못된 리프레시 토큰으로 요청하면 400 에러를 반환한다`(requestBody: String) {
            webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.message").isEqualTo("리프레시 토큰을 입력해주세요")
        }

        @Test
        fun `리프레시 토큰 값을 빈 값으로 요청하면 400 에러를 반환한다`() {
            webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"refresh_token":""}""")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.message").isEqualTo("유효한 리프레시 토큰을 입력해주세요")
        }

        @Test
        fun `만료된 리프레시 토큰으로 요청하면 400 에러를 반환한다`() {
            clock.minusSeconds(jwtProperties.refreshTokenExpiry + 1)
            KakaoApiMockServer.enqueueSuccessResponse()
            val loginResponse = webTestClient.post()
                .uri("/api/v1/social-logins/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"access_token":"valid_kakao_access_token"}""")
                .exchange()
                .expectStatus().isOk
                .returnResult<JsonNode>()
                .responseBody
                .blockFirst()!!

            val refreshToken = loginResponse["data"]["refresh_token"].asText()

            webTestClient.post()
                .uri("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"refresh_token":"$refreshToken"}""")
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.message").isEqualTo("만료된 토큰입니다.")
        }
    }
}
