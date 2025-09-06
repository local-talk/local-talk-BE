package com.localtalk.api.member.entrypoint

import com.localtalk.api.member.domain.Gender
import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.domain.MemberRepository
import com.localtalk.api.member.domain.MemberTerm
import com.localtalk.api.member.domain.Nickname
import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.LocalDate

class NicknameValidationControllerTest : IntegrationTest() {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @BeforeEach
    fun setUp() {
        val existMember = Member(
            nickname = Nickname("existMember"),
            profileImageUrl = "https://example.com/profile.jpg",
            birthDay = LocalDate.of(1990, 1, 1),
            gender = Gender.MALE,
            memberTerm = MemberTerm.forSignup(marketingConsentAgreed = false),
        )
        memberRepository.save(existMember)
    }

    @Nested
    inner class `성공 케이스` {

        @Test
        fun `유효한 닉네임으로 검증 요청 시 성공 응답을 반환한다`() {
            val validNickname = "홍길동"
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$validNickname"}""")
                .exchange()

            response
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("닉네임 검증이 완료되었습니다")
        }
    }

    @Nested
    inner class `형식 검증 실패` {

        @Test
        fun `길이가 부족한 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            val invalidNickname = "a" // 1자 길이 위반
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$invalidNickname"}""")
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("닉네임은 2자 이상 12자 이하여야 합니다")
        }

        @Test
        fun `허용되지 않은 특수문자가 포함된 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            val invalidNickname = "홍길동@"
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$invalidNickname"}""")
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("닉네임은 한글, 영문, 숫자, 공백, _만 사용 가능합니다")
        }

        @Test
        fun `앞뒤 공백이 있는 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            val invalidNickname = " hello"
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$invalidNickname"}""")
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("닉네임은 공백이나 _로 시작할 수 없습니다")
        }

        @Test
        fun `연속된 특수문자가 있는 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            val invalidNickname = "hello  world"
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$invalidNickname"}""")
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("닉네임에 공백이나 _를 연속으로 사용할 수 없습니다")
        }
    }

    @Nested
    inner class `중복 검증 실패` {

        @Test
        fun `중복된 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            val duplicateNickname = "existMember" // setUp에서 생성된 기존 회원 닉네임
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":"$duplicateNickname"}""")
                .exchange()

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("이미 사용 중인 닉네임입니다")
        }
    }

    @Nested
    inner class `요청 검증 실패` {

        @Test
        fun `빈 닉네임으로 검증 요청 시 400 에러를 반환한다`() {
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{"nickname":""}""")
                .exchange()

            response
                .expectStatus().isBadRequest
        }

        @Test
        fun `닉네임이 누락된 요청 시 400 에러를 반환한다`() {
            loginAsTemporaryMember()

            val response = webTestClient
                .post()
                .uri("/api/v1/members/nickname/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""{}""")
                .exchange()

            response
                .expectStatus().isBadRequest
        }
    }
}
