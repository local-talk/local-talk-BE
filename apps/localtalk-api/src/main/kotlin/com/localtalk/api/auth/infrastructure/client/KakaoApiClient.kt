package com.localtalk.api.auth.infrastructure.client

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

@Component
class KakaoApiClient(
    val webClient: WebClient,
    val objectMapper: ObjectMapper,
) {

    companion object {
        private const val KAKAO_TOKEN_INFO_URL = "https://kapi.kakao.com/v1/user/access_token_info"
    }

    suspend fun validateToken(accessToken: String): KakaoAccessTokenQueryResponse {
        return try {
            webClient
                .get()
                .uri(KAKAO_TOKEN_INFO_URL)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono(KakaoAccessTokenQueryResponse::class.java)
                .awaitSingle()
        } catch (e: WebClientResponseException) {
            handleKakaoApiError(e)
        } catch (e: Exception) {
            throw RuntimeException("카카오 토큰 검증 중 예상치 못한 오류가 발생했습니다", e)
        }
    }

    private fun handleKakaoApiError(e: WebClientResponseException): Nothing {
        val errorMessage = try {
            val errorResponse = objectMapper.readValue(e.responseBodyAsString, KakaoApiErrorResponse::class.java)
            "카카오 API 오류 - ${errorResponse.msg} (코드: ${errorResponse.code})"
        } catch (parseException: Exception) {
            "카카오 API 호출 중 오류가 발생했습니다 (HTTP ${e.statusCode.value()})"
        }

        when (e.statusCode.value()) {
            401 -> throw IllegalArgumentException(errorMessage)
            else -> throw RuntimeException(errorMessage)
        }
    }
}
