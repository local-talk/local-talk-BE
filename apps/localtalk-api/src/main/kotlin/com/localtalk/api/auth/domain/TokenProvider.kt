package com.localtalk.api.auth.domain

interface TokenProvider {
    fun generateToken(userId: Long, role: Role): Pair<String, String>
}
