package com.localtalk.api.district.domain

data class Dong(
    val name: String,
    val legalCode: LegalDongCode
) {
    init {
        require(name.isNotBlank()) { "동 이름은 필수입니다" }
    }
}
