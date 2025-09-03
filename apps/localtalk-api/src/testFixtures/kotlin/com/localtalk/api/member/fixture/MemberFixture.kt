package com.localtalk.api.member.fixture

import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.domain.Nickname

object MemberFixture {

    fun createMember(
        nickname: String = "테스트 유저"
    ): Member {
        return Member(
            nickname = Nickname(nickname)
        )
    }
}