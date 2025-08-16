package com.localtalk.api.auth.domain

interface TokenProvider {
    fun generateToken(userId: Long, authRole: AuthRole): Pair<String, String>
}
