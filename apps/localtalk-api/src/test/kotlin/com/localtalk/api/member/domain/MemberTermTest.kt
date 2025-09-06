package com.localtalk.api.member.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MemberTermTest {

    @Nested
    inner class `회원가입용 약관 정보를 생성할 때` {

        @Test
        fun `마케팅 동의시 필수 약관들은 모두 true로 설정된다`() {
            val want = MemberTerm(
                serviceTermsAgreed = true,
                privacyPolicyAgreed = true,
                locationServiceAgreed = true,
                marketingConsentAgreed = true
            )

            val got = MemberTerm.forSignup(marketingConsentAgreed = true)

            assertThat(got).usingRecursiveComparison().isEqualTo(want)
        }

        @Test
        fun `마케팅 거부시 필수 약관은 true이고 마케팅만 false로 설정된다`() {
            val want = MemberTerm(
                serviceTermsAgreed = true,
                privacyPolicyAgreed = true,
                locationServiceAgreed = true,
                marketingConsentAgreed = false
            )

            val got = MemberTerm.forSignup(marketingConsentAgreed = false)

            assertThat(got).usingRecursiveComparison().isEqualTo(want)
        }
    }
}