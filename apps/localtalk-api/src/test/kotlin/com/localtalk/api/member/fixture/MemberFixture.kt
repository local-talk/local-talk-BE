package com.localtalk.api.member.fixture

import com.localtalk.api.district.domain.LegalDongCode
import com.localtalk.api.member.domain.Gender
import com.localtalk.api.member.domain.Member
import java.time.LocalDate

object MemberFixture {

    fun create(
        nickname: String = "테스트유저",
        profileImageUrl: String = "https://example.com/profile.jpg",
        birthDay: LocalDate = LocalDate.of(1990, 1, 1),
        gender: Gender = Gender.MALE,
        marketingConsentAgreed: Boolean = false,
        legalDongCode: String = "1129010100",
        interestIds: List<Long> = emptyList()
    ): Member {
        return Member.signup(
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            birthDay = birthDay,
            gender = gender,
            marketingConsentAgreed = marketingConsentAgreed,
            legalDongCode = LegalDongCode(legalDongCode),
            interestIds = interestIds
        )
    }
}
