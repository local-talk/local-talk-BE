package com.localtalk.api.member.domain

import com.localtalk.api.member.fixture.MemberFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MemberTest {

    @Nested
    inner class `회원가입 정보로 회원을 생성할 때` {

        @Test
        fun `모든 정보가 올바르면 회원과 연관 엔티티들이 생성된다`() {
            val interestIds = listOf(1L, 2L, 3L)

            val got = MemberFixture.create(
                marketingConsentAgreed = true,
                profileImageUrl = "https://example.com/profile.jpg",    
                interestIds = interestIds,
            )

            assertThat(got.nickname.value).isEqualTo("테스트유저")
            assertThat(got.profileImageUrl).isEqualTo("https://example.com/profile.jpg")
            assertThat(got.birthDay).isEqualTo(LocalDate.of(1990, 1, 1))
            assertThat(got.gender).isEqualTo(Gender.MALE)

            assertThat(got.memberTerm.serviceTermsAgreed).isTrue()
            assertThat(got.memberTerm.privacyPolicyAgreed).isTrue()
            assertThat(got.memberTerm.locationServiceAgreed).isTrue()
            assertThat(got.memberTerm.marketingConsentAgreed).isTrue()

            assertThat(got.memberResidences).hasSize(1)
            assertThat(got.memberResidences.first().legalDongCode.value).isEqualTo("1129010100")
            assertThat(got.memberResidences.first().member).isEqualTo(got)

            assertThat(got.memberInterests).hasSize(3)
            val actualInterestIds = got.memberInterests.map { it.interestId }
            assertThat(actualInterestIds).containsExactlyInAnyOrderElementsOf(interestIds)
            got.memberInterests.forEach { memberInterest ->
                assertThat(memberInterest.member).isEqualTo(got)
            }
        }

        @Test
        fun `관심사가 없어도 회원 생성이 정상 처리된다`() {
            val got = MemberFixture.create(
                gender = Gender.FEMALE,
                interestIds = emptyList(),
            )

            assertThat(got.memberInterests).isEmpty()
            assertThat(got.memberResidences).hasSize(1)
        }

        @Test
        fun `마케팅 거부시 해당 설정이 약관에 반영된다`() {
            val got = MemberFixture.create(
                marketingConsentAgreed = false,
                interestIds = listOf(1L),
            )

            assertThat(got.memberTerm.marketingConsentAgreed).isFalse()
            assertThat(got.memberTerm.serviceTermsAgreed).isTrue()
            assertThat(got.memberTerm.privacyPolicyAgreed).isTrue()
            assertThat(got.memberTerm.locationServiceAgreed).isTrue()
        }
    }
}
