package com.localtalk.api.auth.domain.contract

import com.localtalk.api.auth.domain.LoginToken
import org.springframework.data.jpa.repository.JpaRepository

interface LoginTokenRepository : JpaRepository<LoginToken, Long>
