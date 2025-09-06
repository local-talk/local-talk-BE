package com.localtalk.api.member.domain

import com.localtalk.api.district.domain.LegalDongCode
import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import com.localtalk.api.member.infrastructure.NicknameConverter
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import java.time.LocalDate

@Entity
@Table(name = "member")
class Member(
    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    val nickname: Nickname,

    @Column(name = "profile_image_url", nullable = true)
    @Comment("프로필 이미지 URL")
    val profileImageUrl: String?,

    @Column(name = "birth_day", nullable = false)
    @Comment("생년월일")
    val birthDay: LocalDate,

    @Column(name = "gender", nullable = false)
    @Comment("성별")
    val gender: Gender,

    @OneToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    @Comment("회원 약관 동의 정보")
    val memberTerm: MemberTerm,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    val memberResidences: MutableList<MemberResidence> = mutableListOf(),

    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    val memberInterests: MutableList<MemberInterest> = mutableListOf(),

    ) : SoftDeleteBaseEntity() {

    companion object {
        fun signup(
            nickname: String,
            profileImageUrl: String,
            birthDay: LocalDate,
            gender: Gender,
            marketingConsentAgreed: Boolean,
            legalDongCode: LegalDongCode,
            interestIds: List<Long>
        ): Member {
            val memberTerm = MemberTerm.forSignup(marketingConsentAgreed)

            val member = Member(
                nickname = Nickname(nickname),
                profileImageUrl = profileImageUrl,
                birthDay = birthDay,
                gender = gender,
                memberTerm = memberTerm
            )

            val memberResidence = MemberResidence(
                member = member,
                legalDongCode = legalDongCode
            )
            member.memberResidences.add(memberResidence)

            // 관심사 정보 추가
            val memberInterests = interestIds.map { interestId ->
                MemberInterest(
                    member = member,
                    interestId = interestId
                )
            }
            member.memberInterests.addAll(memberInterests)

            return member
        }
    }
}
