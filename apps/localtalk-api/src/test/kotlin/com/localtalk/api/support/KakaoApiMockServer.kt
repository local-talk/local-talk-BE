package com.localtalk.api.support

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class KakaoApiMockServer {

    companion object {
        private val mockWebServer: MockWebServer = MockWebServer().apply { start() }

        init {
            System.setProperty("kakao.api.base-url", mockWebServer.url("/").toString())
        }

        fun enqueueSuccessResponse(id: Long = 1L, expiresIn: Int = 3600, appId: Int = 12345) {
            val responseBody = """
                {
                    "id": $id,
                    "expires_in": $expiresIn,
                    "app_id": $appId
                }
            """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/json")
                    .setBody(responseBody),
            )
            println("MockWebServer: Enqueued success response for id=$id, port=${mockWebServer.port}")
        }

        fun enqueueErrorResponse(code: Int, message: String) {
            val responseBody = """
                {
                    "msg": "$message",
                    "code": $code
                }
            """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(401)
                    .setHeader("Content-Type", "application/json")
                    .setBody(responseBody),
            )
        }
    }
}
