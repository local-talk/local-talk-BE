package com.localtalk.api.member.fixture

import com.localtalk.api.district.domain.LegalDongCode
import com.localtalk.api.district.domain.SeongbukDistrict
import com.localtalk.api.member.domain.Gender
import com.localtalk.api.member.domain.Member
import java.time.LocalDate
import java.util.UUID

object MemberFixture {

    fun create(
        nickname: String = "테스트유저",
        profileImageUrl: String = "https://localtalk-storage.s3.us-east-1.amazonaws.com/profile/${UUID.randomUUID()}.jpg",
        birthDay: LocalDate = LocalDate.of(1990, 1, 1),
        gender: Gender = Gender.MALE,
        marketingConsentAgreed: Boolean = false,
        legalDongCode: String = "1129010100",
        interestIds: List<Long> = emptyList(),
    ): Member {
        return Member.signup(
            nickname = nickname,
            profileImageUrl = profileImageUrl,
            birthDay = birthDay,
            gender = gender,
            marketingConsentAgreed = marketingConsentAgreed,
            legalDongCode = LegalDongCode(legalDongCode),
            interestIds = interestIds,
        )
    }

    fun createSignupRequest(
        marketingConsentAgreed: Boolean = true,
        nickname: String = "테스트유저",
        profileImageUrl: String? = "https://localtalk-storage.s3.us-east-1.amazonaws.com/profile/${UUID.randomUUID()}.jpg",
        birthday: String = "2000-01-01",
        gender: String = Gender.MALE.name,
        legalDongCode: String = SeongbukDistrict.SEONGBUK_DISTRICT.dongs[0].legalCode.value,
        interests: List<Long> = listOf(),
    ): String {
        val url = if (profileImageUrl == null) null else "\"$profileImageUrl\""
        return """
            {
                "marketing_consent_agreed": $marketingConsentAgreed,
                "nickname": "$nickname",
                "profile_image_url": $url,
                "birthday": "$birthday",
                "gender": "$gender",
                "legal_dong_code": "$legalDongCode",
                "interests": [${interests.joinToString(",")}]
            }
       """.trimIndent()
    }
}
