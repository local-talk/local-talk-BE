package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long>
