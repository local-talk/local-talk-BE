package com.localtalk.api.auth.application.dto

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RefreshTokenCommandTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "   ", "\t", "\n"])
    fun `빈 문자열 또는 공백 리프레시 토큰으로 객체 생성 시 예외를 발생시킨다`(invalidToken: String) {
        assertThatThrownBy { RefreshTokenCommand(invalidToken) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("유효한 리프레시 토큰을 입력해주세요")
    }
}
