package com.localtalk.api.auth.infrastructure.client

import com.localtalk.api.auth.domain.TokenValidationInfo
import com.localtalk.api.auth.domain.TokenValidationStrategy
import org.springframework.stereotype.Component

@Component
class KakaoTokenValidationStrategy(
    val kakaoApiClient: KakaoApiClient,
) : TokenValidationStrategy {

    override val supportedProviderName: String = "KAKAO"

    override suspend fun validate(accessToken: String): TokenValidationInfo {
        val response = kakaoApiClient.validateToken(accessToken)

        return TokenValidationInfo(
            provider = "KAKAO",
            socialKey = response.id.toString(),
        )
    }
}
