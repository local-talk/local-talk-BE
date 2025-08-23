package com.localtalk.api.member.domain

@JvmInline
value class ProfileUrl(val value: String) {

    init {
        require(value.isNotBlank()) { "프로필 URL은 비어있을 수 없습니다" }
        require(value.matches(URL_PATTERN)) { "올바른 프로필 URL 형식이 아닙니다" }
    }

    companion object {
        private val URL_PATTERN = Regex("^https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+$")
    }
}
