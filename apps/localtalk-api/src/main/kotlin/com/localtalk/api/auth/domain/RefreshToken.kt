package com.localtalk.api.auth.domain

import com.localtalk.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Column(name = "token", nullable = false)
    val token: String,
) : BaseEntity()
