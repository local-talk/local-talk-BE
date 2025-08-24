package com.localtalk.api.district.domain

data class District(
    val name: String,
    val dongs: List<Dong>
) {
    init {
        require(name.isNotBlank()) { "구 이름은 필수입니다" }
        require(dongs.isNotEmpty()) { "동 목록은 비어있을 수 없습니다" }
    }
}
