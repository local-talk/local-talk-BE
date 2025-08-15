package com.localtalk.api.auth.infrastructure.strategy

import com.localtalk.api.auth.domain.TokenValidationInfo
import com.localtalk.api.auth.domain.TokenValidationStrategy
import com.localtalk.api.auth.infrastructure.client.KakaoApiClient
import org.springframework.stereotype.Component

@Component
class KakaoTokenValidationStrategy(
    val kakaoApiClient: KakaoApiClient,
) : TokenValidationStrategy {

    override val supportedProviderName: String = "KAKAO"

    override suspend fun validate(accessToken: String): TokenValidationInfo {
        TODO()
    }
}
