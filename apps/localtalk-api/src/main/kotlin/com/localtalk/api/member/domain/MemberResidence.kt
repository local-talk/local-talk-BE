package com.localtalk.api.member.domain

import com.localtalk.api.district.domain.LegalDongCode
import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "member_residence")
class MemberResidence(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 ID")
    val member: Member,

    @Comment("법정동 코드")
    val legalDongCode: LegalDongCode,
) : SoftDeleteBaseEntity()