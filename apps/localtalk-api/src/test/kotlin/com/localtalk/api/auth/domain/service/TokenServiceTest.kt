package com.localtalk.api.auth.domain.service

import com.localtalk.api.auth.domain.contract.RefreshTokenRepository
import com.localtalk.api.auth.domain.contract.TokenProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TokenServiceTest {

    private val tokenProvider = mockk<TokenProvider>()
    private val refreshTokenRepository = mockk<RefreshTokenRepository>()
    private val tokenService = TokenService(tokenProvider, refreshTokenRepository)

    @Nested
    inner class `리프레시 토큰을 전달했을 때` {

        @Test
        fun `해당 토큰 정보가 존재하지 않는 경우 IllegalArgumentException을 발생시킨다`() {
            val nonExistentToken = "non_existent_token"
            
            every { refreshTokenRepository.findByToken(nonExistentToken) } returns null

            assertThatThrownBy { tokenService.rotateRefreshToken(nonExistentToken) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("존재하지 않는 토큰입니다.")
        }
    }
}
