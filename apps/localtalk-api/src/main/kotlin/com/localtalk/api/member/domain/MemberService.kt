package com.localtalk.api.member.domain

import org.springframework.stereotype.Service

@Service
class MemberService(
    val memberRepository: MemberRepository,
) {

    fun isNicknameExists(nickname: Nickname): Boolean {
        return memberRepository.existsByNickname(nickname)
    }
}