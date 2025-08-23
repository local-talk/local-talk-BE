package com.localtalk.api.member.entrypoint.mapper

import com.localtalk.api.member.domain.Nickname
import com.localtalk.api.member.entrypoint.dto.NicknameValidationRequest
import org.springframework.stereotype.Component

@Component
class NicknameValidationRestMapper {

    fun toNickname(request: NicknameValidationRequest): Nickname {
        return Nickname(request.nickname!!)
    }
}