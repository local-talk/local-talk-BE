package com.localtalk.api.auth.infrastructure.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoApiClient(
    val webClient: WebClient
) {

    suspend fun validateToken(accessToken: String): KakaoUserInfo {
        TODO()
    }
}

data class KakaoUserInfo(
    val id: Long,
    val connectedAt: String?,
    val kakaoAccount: KakaoAccount?
)

data class KakaoAccount(
    val profile: KakaoProfile?
)

data class KakaoProfile(
    val nickname: String?,
    val profileImageUrl: String?
)
