package com.localtalk.api.member.application

import com.localtalk.api.member.domain.MemberService
import com.localtalk.api.member.domain.Nickname
import org.springframework.stereotype.Service

@Service
class MemberApplicationService(
    val memberService: MemberService,
) {

    fun validateNickname(nickname: Nickname) {
        require(!memberService.isNicknameExists(nickname)) { "이미 사용 중인 닉네임입니다" }
    }
}

