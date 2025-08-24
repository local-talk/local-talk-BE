package com.localtalk.internal

@RequiresOptIn(
    message = "BaseEntity 대신 SoftDeleteBaseEntity 또는 HardDeleteBaseEntity를 사용하세요.",
    level = RequiresOptIn.Level.ERROR,
)
annotation class InternalBaseEntityApi
