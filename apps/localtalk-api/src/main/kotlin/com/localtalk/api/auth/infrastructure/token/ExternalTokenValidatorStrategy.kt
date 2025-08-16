package com.localtalk.api.auth.infrastructure.token

import com.localtalk.api.auth.domain.ExternalTokenValidator
import com.localtalk.api.auth.domain.TokenValidationInfo
import com.localtalk.api.auth.domain.TokenValidationStrategy
import org.springframework.stereotype.Component

@Component
class ExternalTokenValidatorStrategy(
    strategies: List<TokenValidationStrategy>,
) : ExternalTokenValidator {

    val strategyMap: Map<String, TokenValidationStrategy> =
        strategies.associateBy { it.supportedProviderName }

    override suspend fun validateToken(accessToken: String, providerName: String): TokenValidationInfo {
        val strategy = strategyMap[providerName]
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 프로바이더입니다: $providerName")

        return strategy.validate(accessToken)
    }
}
