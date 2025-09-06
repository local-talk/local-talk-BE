package com.localtalk.api.member.domain

import com.localtalk.api.district.domain.LegalDongCode
import com.localtalk.api.member.application.dto.DistrictCommand
import com.localtalk.api.member.application.dto.InterestCommand
import com.localtalk.api.member.application.dto.MemberCommand
import com.localtalk.api.member.application.dto.TermCommand
import org.springframework.stereotype.Service

@Service
class MemberService(
    val memberRepository: MemberRepository,
) {

    fun isNicknameExists(nickname: Nickname): Boolean {
        return memberRepository.existsByNickname(nickname)
    }

    fun signup(
        memberCommand: MemberCommand,
        termCommand: TermCommand,
        profileImageUrl: String,
        districtCommand: DistrictCommand,
        interestCommand: InterestCommand,
    ): Member {
        val member = Member.signup(
            nickname = memberCommand.nickname,
            profileImageUrl = profileImageUrl,
            birthDay = memberCommand.birthday,
            gender = memberCommand.gender,
            marketingConsentAgreed = termCommand.marketingConsentAgreed,
            legalDongCode = LegalDongCode(districtCommand.legalDongCode),
            interestIds = interestCommand.interestIds
        )

        return memberRepository.save(member)
    }
}
