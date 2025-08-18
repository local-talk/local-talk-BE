package com.localtalk.api.auth.application

import com.localtalk.api.auth.application.dto.SocialLoginCommand
import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.LoginToken
import com.localtalk.api.auth.domain.SocialLogin
import com.localtalk.api.auth.domain.SocialLoginIdentifier
import com.localtalk.api.auth.domain.SocialLoginProvider
import com.localtalk.api.auth.domain.contract.ExternalTokenValidator
import com.localtalk.api.auth.domain.service.SocialLoginService
import com.localtalk.api.auth.domain.service.TokenService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SocialLoginApplicationServiceTest {

    @MockK
    lateinit var socialLoginService: SocialLoginService

    @MockK
    lateinit var tokenService: TokenService

    @MockK
    lateinit var externalTokenValidator: ExternalTokenValidator

    @InjectMockKs
    lateinit var socialLoginApplicationService: SocialLoginApplicationService

    @Nested
    inner class `소셜 로그인을 처리할 때` {

        @Test
        fun `기존 가입 사용자라면 Member 권한으로 토큰을 생성한다`() {
            runBlocking {
                val command = SocialLoginCommand(SocialLoginProvider.KAKAO, "access-token")
                val validationResult = SocialLoginIdentifier("KAKAO", "social-key-123")
                val existingSocialLogin = SocialLogin(SocialLoginProvider.KAKAO, "social-key-123", memberId = 100L)
                val memberToken = LoginToken("member-access-token", "member-refresh-token")

                coEvery { externalTokenValidator.validateToken("access-token", "KAKAO") } returns validationResult
                every { socialLoginService.findByProviderAndSocialKey(SocialLoginProvider.KAKAO, "social-key-123") } returns existingSocialLogin
                every { tokenService.generateToken(100L, AuthRole.MEMBER) } returns memberToken

                val result = socialLoginApplicationService.processSocialLogin(command)

                assertThat(result.isSignedUser).isTrue()
            }
        }

        @Test
        fun `신규 사용자라면 Temporary 권한으로 토큰을 생성한다`() {
            runBlocking {
                val command = SocialLoginCommand(SocialLoginProvider.KAKAO, "access-token")
                val validationResult = SocialLoginIdentifier("KAKAO", "social-key-456")
                val newSocialLogin = SocialLogin(SocialLoginProvider.KAKAO, "social-key-456")

                coEvery { externalTokenValidator.validateToken("access-token", "KAKAO") } returns validationResult
                every { socialLoginService.findByProviderAndSocialKey(SocialLoginProvider.KAKAO, "social-key-456") } returns null
                every { socialLoginService.create(SocialLoginProvider.KAKAO, "social-key-456") } returns newSocialLogin
                every { tokenService.generateToken(any(), AuthRole.TEMPORARY) } returns LoginToken("temp-access", "temp-refresh")

                val result = socialLoginApplicationService.processSocialLogin(command)

                assertThat(result.isSignedUser).isFalse()
            }
        }

        @Test
        fun `기존 소셜 로그인이지만 미가입 사용자라면 Temporary 권한으로 토큰을 생성한다`() {
            runBlocking {
                val command = SocialLoginCommand(SocialLoginProvider.APPLE, "access-token")
                val validationResult = SocialLoginIdentifier("APPLE", "social-key-789")
                val unregisteredSocialLogin = SocialLogin(SocialLoginProvider.APPLE, "social-key-789")

                coEvery { externalTokenValidator.validateToken("access-token", "APPLE") } returns validationResult
                every { socialLoginService.findByProviderAndSocialKey(SocialLoginProvider.APPLE, "social-key-789") } returns unregisteredSocialLogin
                every { tokenService.generateToken(any(), AuthRole.TEMPORARY) } returns LoginToken("temp-access", "temp-refresh")

                val result = socialLoginApplicationService.processSocialLogin(command)

                assertThat(result.isSignedUser).isFalse()
            }
        }
    }
}
