package com.localtalk.api.auth.infrastructure.client

data class KakaoApiErrorResponse(
    val msg: String,
    val code: Int,
)