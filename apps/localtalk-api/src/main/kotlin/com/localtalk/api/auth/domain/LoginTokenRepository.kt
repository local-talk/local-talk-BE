package com.localtalk.api.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LoginTokenRepository : JpaRepository<LoginToken, Long>
