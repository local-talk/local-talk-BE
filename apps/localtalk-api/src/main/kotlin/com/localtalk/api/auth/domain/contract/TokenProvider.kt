package com.localtalk.api.auth.domain.contract

interface TokenProvider {
    fun generateToken(id: Long, role: String): Pair<String, String>
}
