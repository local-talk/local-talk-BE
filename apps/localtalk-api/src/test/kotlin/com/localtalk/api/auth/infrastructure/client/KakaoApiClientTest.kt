package com.localtalk.api.auth.infrastructure.client

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.reactive.function.client.WebClient

class KakaoApiClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var webClient: WebClient
    private lateinit var kakaoApiClient: TestKakaoApiClient
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        webClient = WebClient.builder()
            .build()
            
        kakaoApiClient = TestKakaoApiClient(webClient, objectMapper, mockWebServer.url("/").toString())
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Nested
    inner class `카카오 토큰 검증을 할 때` {

        @Test
        fun `유효한 토큰이면 사용자 정보를 반환한다`() {
            runBlocking {
            val expectedResponse = KakaoAccessTokenQueryResponse(
                id = 12345L,
                expiresIn = 7200L,
                appId = 678910L
            )

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("""
                        {
                            "id": 12345,
                            "expires_in": 7200,
                            "app_id": 678910
                        }
                    """.trimIndent())
                    .addHeader("Content-Type", "application/json")
            )

            val result = kakaoApiClient.validateToken("valid-access-token")

            assertThat(result.id).isEqualTo(expectedResponse.id)
            assertThat(result.expiresIn).isEqualTo(expectedResponse.expiresIn)
            assertThat(result.appId).isEqualTo(expectedResponse.appId)

            val request = mockWebServer.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.path).isEqualTo("/v1/user/access_token_info")
            assertThat(request.getHeader("Authorization")).isEqualTo("Bearer valid-access-token")
            }
        }

        @Test
        fun `유효하지 않은 토큰이면 IllegalArgumentException을 던진다`() {
            runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(401)
                    .setBody("""
                        {
                            "msg": "this access token does not exist",
                            "code": -401
                        }
                    """.trimIndent())
                    .addHeader("Content-Type", "application/json")
            )

            val exception = assertThrows<IllegalArgumentException> {
                runBlocking {
                    kakaoApiClient.validateToken("invalid-access-token")
                }
            }

            assertThat(exception.message).isEqualTo("카카오 API 오류 - this access token does not exist (코드: -401)")
            }
        }

        @Test
        fun `서버 오류가 발생하면 RuntimeException을 던진다`() {
            runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(500)
                    .setBody("Internal Server Error")
            )

            val exception = assertThrows<RuntimeException> {
                runBlocking {
                    kakaoApiClient.validateToken("some-access-token")
                }
            }

            assertThat(exception.message).contains("카카오 API 호출 중 오류가 발생했습니다")
            }
        }

        @Test
        fun `카카오 API 에러 형식으로 응답이 오면 상세 메시지를 포함한다`() {
            runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody("""
                        {
                            "msg": "invalid request",
                            "code": -2
                        }
                    """.trimIndent())
                    .addHeader("Content-Type", "application/json")
            )

            val exception = assertThrows<RuntimeException> {
                runBlocking {
                    kakaoApiClient.validateToken("some-access-token")
                }
            }

            assertThat(exception.message).isEqualTo("카카오 API 오류 - invalid request (코드: -2)")
            }
        }
    }
}