package com.localtalk.api.member.domain

import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "member_interest")
class MemberInterest(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 ID")
    val member: Member,

    @Column(name = "interest_id", nullable = false)
    @Comment("관심사 ID")
    val interestId: Long,
) : SoftDeleteBaseEntity()