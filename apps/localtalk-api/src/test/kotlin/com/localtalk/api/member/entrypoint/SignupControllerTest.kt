package com.localtalk.api.member.entrypoint

import com.localtalk.api.member.fixture.MemberFixture
import com.localtalk.api.support.IntegrationTest
import com.localtalk.api.utils.JwtTestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class SignupControllerTest : IntegrationTest() {

    @Nested
    inner class `회원가입 요청 시` {

        @Test
        fun `인증된 사용자가 올바른 정보로 요청하면 회원가입에 성공한다`() {
            loginAsTemporaryMember()
            val request = MemberFixture.createSignupRequest(profileImageUrl = null)
            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("회원가입이 완료되었습니다")
                .jsonPath("$.data.access_token").value<String> { assertThat(JwtTestUtils.isValidJwtFormat(it)).isTrue }
                .jsonPath("$.data.refresh_token").value<String> { assertThat(JwtTestUtils.isValidJwtFormat(it)).isTrue }
        }

        @Test
        fun `관심사 없이도 회원가입에 성공한다`() {
            loginAsTemporaryMember()
            val request = MemberFixture.createSignupRequest(
                nickname = "테스트유저2",
                birthday = "1985-05-15",
                gender = "FEMALE",
                profileImageUrl = null,
                marketingConsentAgreed = false,
                legalDongCode = "1129010200",
                interests = emptyList(),
            )

            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("회원가입이 완료되었습니다")
                .jsonPath("$.data.access_token").value<String> { assertThat(JwtTestUtils.isValidJwtFormat(it)).isTrue }
                .jsonPath("$.data.refresh_token").value<String> { assertThat(JwtTestUtils.isValidJwtFormat(it)).isTrue }
        }

        @Test
        fun `잘못된 성별 값으로 요청시 400 오류를 반환한다`() {
            loginAsTemporaryMember()
            val invalidGender = "INVALID_GENDER"
            val request = MemberFixture.createSignupRequest(
                nickname = "테스트유저",
                birthday = "1990-01-01",
                gender = invalidGender,
                marketingConsentAgreed = true,
                legalDongCode = "1129010100",
                interests = listOf(1, 2, 3),
            )

            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("지원하지 않는 성별입니다: $invalidGender. 가능한 값: MALE, FEMALE, NONE")
        }

        @Test
        fun `잘못된 법정동 코드로 요청시 400 오류를 반환한다`() {
            loginAsTemporaryMember()
            val invalidCode = "invalid_code"
            val request = MemberFixture.createSignupRequest(
                nickname = "테스트유저",
                birthday = "1990-01-01",
                gender = "MALE",
                marketingConsentAgreed = true,
                legalDongCode = invalidCode,
                interests = listOf(1, 2, 3),
            )

            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("올바른 법정동 코드 형식이 아닙니다: $invalidCode")
        }

        @Test
        fun `인증되지 않은 사용자는 401 오류를 반환한다`() {
            val request = MemberFixture.createSignupRequest(
                nickname = "테스트유저",
                birthday = "1990-01-01",
                gender = "MALE",
                marketingConsentAgreed = true,
                legalDongCode = "1129010100",
                interests = listOf(1, 2, 3),
            )

            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized
        }

        @Test
        fun `MEMBER 권한으로 회원가입 요청시 403 오류를 반환한다`() {
            loginAsMember()
            val request = MemberFixture.createSignupRequest(
                nickname = "이미가입완료",
                birthday = "1990-01-01",
                profileImageUrl = null,
                gender = "MALE",
                marketingConsentAgreed = true,
                legalDongCode = "1129010100",
                interests = listOf(1, 2),
            )

            webTestClient.post()
                .uri("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden
        }
    }


}
