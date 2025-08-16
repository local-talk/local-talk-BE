package com.localtalk.api.auth.infrastructure.client

import com.localtalk.api.auth.domain.SocialLoginIdentifier
import com.localtalk.api.auth.infrastructure.token.TokenValidationStrategy
import org.springframework.stereotype.Component

@Component
class KakaoTokenValidationStrategy(
    val kakaoApiClient: KakaoApiClient,
) : TokenValidationStrategy {

    override val provider: String = "KAKAO"

    override suspend fun validate(accessToken: String): SocialLoginIdentifier {
        val response = kakaoApiClient.validateToken(accessToken)

        return SocialLoginIdentifier(
            provider = "KAKAO",
            socialKey = response.id.toString(),
        )
    }
}
