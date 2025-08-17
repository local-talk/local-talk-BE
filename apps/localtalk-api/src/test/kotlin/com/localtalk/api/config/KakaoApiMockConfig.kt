package com.localtalk.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.localtalk.api.auth.infrastructure.client.KakaoAccessTokenQueryResponse
import okhttp3.mockwebserver.MockResponse
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class KakaoApiMockConfig {

    fun createSuccessResponse(objectMapper: ObjectMapper, socialId: Long = 123456789L): MockResponse {
        val responseBody = KakaoAccessTokenQueryResponse(
            id = socialId,
            expiresIn = 3600,
            appId = 12345
        )
        return MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody(objectMapper.writeValueAsString(responseBody))
    }

    fun createErrorResponse(statusCode: Int = 401, errorCode: Int = -401, message: String = "Invalid token"): MockResponse {
        val errorBody = """
            {
                "code": $errorCode,
                "msg": "$message"
            }
        """.trimIndent()
        
        return MockResponse()
            .setResponseCode(statusCode)
            .setHeader("Content-Type", "application/json")
            .setBody(errorBody)
    }
}