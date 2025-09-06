package com.localtalk.api.member.entrypoint

import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class SignupControllerTest : IntegrationTest() {

    @Nested
    inner class `회원가입 요청 시` {

        @Test
        fun `인증된 사용자가 올바른 정보로 요청하면 회원가입에 성공한다`() {
            val authenticatedClient = loginAsTemporaryMember()

            authenticatedClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "테스트유저")
                        .queryParam("birthday", "1990-01-01")
                        .queryParam("gender", "MALE")
                        .queryParam("marketing_consent_agreed", "true")
                        .queryParam("legal_dong_code", "1129010100")
                        .queryParam("interests", "1,2,3")
                        .build()
                }
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("회원가입이 완료되었습니다")
        }

        @Test
        fun `관심사 없이도 회원가입에 성공한다`() {
            val authenticatedClient = loginAsTemporaryMember()

            authenticatedClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "테스트유저2")
                        .queryParam("birthday", "1985-05-15")
                        .queryParam("gender", "FEMALE")
                        .queryParam("marketing_consent_agreed", "false")
                        .queryParam("legal_dong_code", "1129010200")
                        .queryParam("interests", "")
                        .build()
                }
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("회원가입이 완료되었습니다")
        }

        @Test
        fun `잘못된 성별 값으로 요청시 400 오류를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()

            authenticatedClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "테스트유저")
                        .queryParam("birthday", "1990-01-01")
                        .queryParam("gender", "INVALID_GENDER")  // 잘못된 성별
                        .queryParam("marketing_consent_agreed", "true")
                        .queryParam("legal_dong_code", "1129010100")
                        .queryParam("interests", "1,2,3")
                        .build()
                }
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `잘못된 법정동 코드로 요청시 400 오류를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()

            authenticatedClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "테스트유저")
                        .queryParam("birthday", "1990-01-01")
                        .queryParam("gender", "MALE")
                        .queryParam("marketing_consent_agreed", "true")
                        .queryParam("legal_dong_code", "invalid_code")  // 잘못된 법정동 코드
                        .queryParam("interests", "1,2,3")
                        .build()
                }
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `인증되지 않은 사용자는 401 오류를 반환한다`() {
            webTestClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "테스트유저")
                        .queryParam("birthday", "1990-01-01")
                        .queryParam("gender", "MALE")
                        .queryParam("marketing_consent_agreed", "true")
                        .queryParam("legal_dong_code", "1129010100")
                        .queryParam("interests", "1,2,3")
                        .build()
                }
                .exchange()
                .expectStatus().isUnauthorized
        }
    }

    @Nested
    inner class `권한 검증 시` {

        @Test
        fun `TEMPORARY 권한으로 회원가입에 성공한다`() {
            val temporaryClient = loginAsTemporaryMember()

            temporaryClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/v1/signup")
                        .queryParam("nickname", "임시유저")
                        .queryParam("birthday", "1990-01-01")
                        .queryParam("gender", "MALE")
                        .queryParam("marketing_consent_agreed", "true")
                        .queryParam("legal_dong_code", "1129010100")
                        .queryParam("interests", "1,2")
                        .build()
                }
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("회원가입이 완료되었습니다")
        }

    }
}
