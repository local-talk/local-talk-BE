package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.AuthMember

interface TokenProvider {
    fun generateToken(id: Long, role: String): Pair<String, String>
    fun parseToken(token: String): AuthMember
}
