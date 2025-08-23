package com.localtalk.api.member.domain


private val ALLOWED_CHARS = Regex("^[가-힣a-zA-Z0-9 _]+$")
private val CONSECUTIVE_SPECIAL_CHARS = Regex("[ _]{2,}")

@JvmInline
value class Nickname(val value: String) {
    init {
        require(value.length in 2..12) { "닉네임은 2자 이상 12자 이하여야 합니다" }

        require(value.matches(ALLOWED_CHARS)) { "닉네임은 한글, 영문, 숫자, 공백, _만 사용 가능합니다" }

        require(!value.startsWith(' ') && !value.startsWith('_')) { "닉네임은 공백이나 _로 시작할 수 없습니다" }

        require(!value.endsWith(' ') && !value.endsWith('_')) { "닉네임은 공백이나 _로 끝날 수 없습니다" }

        require(!value.contains(CONSECUTIVE_SPECIAL_CHARS)) { "닉네임에 공백이나 _를 연속으로 사용할 수 없습니다" }
    }

}
