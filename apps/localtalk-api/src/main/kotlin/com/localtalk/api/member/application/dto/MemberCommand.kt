package com.localtalk.api.member.application.dto

import com.localtalk.api.member.domain.Gender
import java.time.LocalDate

data class MemberCommand(
    val nickname: String,
    val birthday: LocalDate,
    val gender: Gender,
) {
    init {
        require(nickname.isNotBlank()) { "닉네임은 빈 값일 수 없습니다" }
        require(nickname.length in 2..20) { "닉네임은 2-20자 사이여야 합니다" }
        require(birthday.isBefore(LocalDate.now())) { "생년월일은 미래일 수 없습니다" }
    }
}