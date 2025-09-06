package com.localtalk.api.auth.domain

data class AuthenticatedUser(
    val id: Long,
    val role: AuthRole,
) {
    fun isMember(): Boolean = role == AuthRole.MEMBER

    fun isTemporary(): Boolean = role == AuthRole.TEMPORARY
}
