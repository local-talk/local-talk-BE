package com.localtalk.api.auth.domain

import com.localtalk.api.auth.domain.contract.LoginTokenRepository
import com.localtalk.api.auth.domain.contract.TokenProvider
import com.localtalk.api.auth.domain.service.TokenService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TokenServiceTest {

    @MockK
    lateinit var tokenProvider: TokenProvider

    @MockK
    lateinit var loginTokenRepository: LoginTokenRepository

    @InjectMockKs
    lateinit var tokenService: TokenService

    @Nested
    inner class `토큰을 생성할 때` {

        @Test
        fun `TokenProvider로 토큰 생성 후 Repository에 저장한다`() {
            val userId = 123L
            val authRole = AuthRole.MEMBER
            val accessToken = "access-token"
            val refreshToken = "refresh-token"
            val savedToken = LoginToken(accessToken, refreshToken)

            every { tokenProvider.generateToken(userId, authRole) } returns (accessToken to refreshToken)
            every { loginTokenRepository.save(any<LoginToken>()) } returns savedToken

            val result = tokenService.generateToken(userId, authRole)

            assertThat(result).isEqualTo(savedToken)
            verify { tokenProvider.generateToken(userId, authRole) }
            verify { loginTokenRepository.save(any<LoginToken>()) }
        }
    }
}
