package com.localtalk.api.auth.entrypoint.dto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class TokenRefreshRequestTest {

    @Test
    fun `유효한 리프레시 토큰으로 객체를 생성한다`() {
        val want = TokenRefreshRequest("valid_refresh_token")

        assertThat(want.refreshToken).isEqualTo("valid_refresh_token")
    }

    @Test
    fun `null 리프레시 토큰으로 객체 생성 시 예외를 발생시킨다`() {
        assertThatThrownBy { TokenRefreshRequest(null) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("리프레시 토큰을 입력해주세요")
    }
}
