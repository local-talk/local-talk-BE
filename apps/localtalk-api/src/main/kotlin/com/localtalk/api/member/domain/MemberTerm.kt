package com.localtalk.api.member.domain

import com.localtalk.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import java.time.ZonedDateTime

@Entity
@Table(name = "member_terms_agreement")
class MemberTermsAgreement(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 ID")
    val member: Member,

    @Column(name = "service_terms_agreed_at", nullable = false)
    @Comment("서비스 이용약관 동의 일시")
    val serviceTermsAgreedAt: ZonedDateTime,

    @Column(name = "privacy_policy_agreed_at", nullable = false)
    @Comment("개인정보 처리방침 동의 일시")
    val privacyPolicyAgreedAt: ZonedDateTime,

    @Column(name = "location_service_agreed_at", nullable = false)
    @Comment("위치기반 서비스 이용약관 동의 일시")
    val locationServiceAgreedAt: ZonedDateTime,

    @Column(name = "marketing_consent_agreed", nullable = false)
    @Comment("마케팅 정보 수신 동의 여부")
    val marketingConsentAgreed: Boolean,

    @Column(name = "marketing_consent_agreed_at", nullable = true)
    @Comment("마케팅 정보 수신 동의 일시")
    val marketingConsentAgreedAt: ZonedDateTime?
) : BaseEntity() {

    override fun validate() {
        if (marketingConsentAgreed) {
            requireNotNull(marketingConsentAgreedAt) { "마케팅 동의 시 동의 일시는 필수입니다" }
        }
    }

    fun updateMarketingConsent(newConsent: Boolean, agreedAt: ZonedDateTime = ZonedDateTime.now()): MemberTermsAgreement {
        return MemberTermsAgreement(
            member = member,
            serviceTermsAgreedAt = serviceTermsAgreedAt,
            privacyPolicyAgreedAt = privacyPolicyAgreedAt,
            locationServiceAgreedAt = locationServiceAgreedAt,
            marketingConsentAgreed = newConsent,
            marketingConsentAgreedAt = if (newConsent) agreedAt else null
        )
    }

    fun hasAgreedToAllRequiredTerms(): Boolean = true // 레코드 존재 자체가 필수 약관 동의 의미

    fun getRequiredTermsAgreedDates(): List<ZonedDateTime> = listOf(
        serviceTermsAgreedAt,
        privacyPolicyAgreedAt,
        locationServiceAgreedAt
    )

    companion object {
        fun forSignUp(
            member: Member,
            marketingConsent: Boolean = false,
            agreedAt: ZonedDateTime = ZonedDateTime.now()
        ): MemberTermsAgreement {
            return MemberTermsAgreement(
                member = member,
                serviceTermsAgreedAt = agreedAt,
                privacyPolicyAgreedAt = agreedAt,
                locationServiceAgreedAt = agreedAt,
                marketingConsentAgreed = marketingConsent,
                marketingConsentAgreedAt = if (marketingConsent) agreedAt else null
            )
        }

        fun withMarketingConsent(
            member: Member,
            agreedAt: ZonedDateTime = ZonedDateTime.now()
        ): MemberTermsAgreement {
            return forSignUp(member, marketingConsent = true, agreedAt)
        }

        fun withoutMarketingConsent(
            member: Member,
            agreedAt: ZonedDateTime = ZonedDateTime.now()
        ): MemberTermsAgreement {
            return forSignUp(member, marketingConsent = false, agreedAt)
        }

        fun renewAllTerms(
            member: Member,
            marketingConsent: Boolean,
            agreedAt: ZonedDateTime = ZonedDateTime.now()
        ): MemberTermsAgreement {
            return forSignUp(member, marketingConsent, agreedAt)
        }
    }
}
