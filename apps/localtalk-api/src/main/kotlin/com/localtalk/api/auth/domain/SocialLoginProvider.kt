package com.localtalk.api.auth.domain

enum class SocialLoginProvider(
    val description: String,
) {
    KAKAO("카카오"),
    APPLE("애플");

    companion object {
        private val providerMap = entries.associateBy { it.name.uppercase() }

        fun fromString(providerString: String): SocialLoginProvider {
            val normalizedProvider = providerString.uppercase()
            return providerMap[normalizedProvider]
                ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 프로바이더입니다: $providerString")
        }
    }
}
