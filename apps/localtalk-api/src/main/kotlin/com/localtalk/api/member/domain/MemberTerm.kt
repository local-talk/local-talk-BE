package com.localtalk.api.member.domain

import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "member_term")
class MemberTerm(

    @Column(name = "service_terms_agreed", nullable = false)
    @Comment("서비스 이용약관 동의 일시")
    val serviceTermsAgreed: Boolean,

    @Column(name = "privacy_policy_agreed", nullable = false)
    @Comment("개인정보 처리방침 동의 일시")
    val privacyPolicyAgreed: Boolean,

    @Column(name = "location_service_agreed", nullable = false)
    @Comment("위치기반 서비스 이용약관 동의 일시")
    val locationServiceAgreed: Boolean,

    @Column(name = "marketing_consent_agreed", nullable = false)
    @Comment("마케팅 정보 수신 동의 여부")
    val marketingConsentAgreed: Boolean,
) : SoftDeleteBaseEntity() {
    companion object {
        /**
         * 회원가입 시 필수 약관 동의와 선택적 마케팅 동의로 약관 정보를 생성
         * 서비스 이용약관, 개인정보처리방침, 위치서비스 약관은 회원가입 시 필수 동의
         */
        fun forSignup(marketingConsentAgreed: Boolean): MemberTerm {
            return MemberTerm(
                serviceTermsAgreed = true,
                privacyPolicyAgreed = true,
                locationServiceAgreed = true,
                marketingConsentAgreed = marketingConsentAgreed
            )
        }
    }
}
