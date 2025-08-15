package com.localtalk.api.auth.domain

import com.localtalk.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "social_login")
class SocialLogin(
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    @Comment("소셜 로그인 제공자")
    val provider: SocialLoginProvider,

    @Column(name = "social_key", nullable = false)
    @Comment("소셜 로그인 식별자")
    val socialKey: String,

    @Column(name = "member_id")
    @Comment("연결된 회원 ID")
    var memberId: Long? = null,
) : BaseEntity() {
    fun isSignedUser(): Boolean = memberId != null
}
