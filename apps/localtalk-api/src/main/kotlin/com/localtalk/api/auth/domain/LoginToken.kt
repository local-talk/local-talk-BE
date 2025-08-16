package com.localtalk.api.auth.domain

import com.localtalk.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "login_token")
class LoginToken(
    @Column(name = "refresh_token", nullable = false)
    val refreshToken: String,
) : BaseEntity()
