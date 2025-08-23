package com.localtalk.api.member.domain

import com.localtalk.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "member")
class Member(
    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    val nickname: Nickname,
) : BaseEntity()
