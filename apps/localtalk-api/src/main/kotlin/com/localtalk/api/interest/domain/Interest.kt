package com.localtalk.api.interest.domain

import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "interest")
class Interest(
    @Column(name = "name", nullable = false, length = 50)
    @Comment("관심사 이름")
    val name: String,

    @Column(name = "emoji", nullable = false, length = 10)
    @Comment("관심사 이모지")
    val emoji: String,

    @Column(name = "display_order", nullable = false, unique = true)
    @Comment("표시 순서")
    val displayOrder: Int,
) : SoftDeleteBaseEntity()
