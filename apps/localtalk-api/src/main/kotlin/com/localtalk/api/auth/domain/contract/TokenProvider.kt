package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.AuthRole

interface TokenProvider {
    fun generateToken(userId: Long, authRole: AuthRole): Pair<String, String>
}
