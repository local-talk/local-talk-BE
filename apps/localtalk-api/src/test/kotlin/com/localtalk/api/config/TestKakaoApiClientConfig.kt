package com.localtalk.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.localtalk.api.auth.infrastructure.client.KakaoApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@TestConfiguration
class TestKakaoApiClientConfig {

    @Bean
    @Primary
    fun testKakaoApiClient(
        objectMapper: ObjectMapper,
        @Value("\${kakao.api.base-url}") baseUrl: String
    ): KakaoApiClient {
        val webClient = WebClient.builder()
            .baseUrl("http://localhost:8080") // 기본값, 테스트에서 동적으로 변경
            .build()
        
        return KakaoApiClient(webClient, objectMapper, baseUrl)
    }
}