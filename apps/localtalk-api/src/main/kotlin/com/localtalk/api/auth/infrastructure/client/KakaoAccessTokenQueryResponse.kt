package com.localtalk.api.auth.infrastructure.client

data class KakaoAccessTokenQueryResponse(
    val id: Long,
    val expiresIn: Long,
    val appId: Long,
)
