package com.localtalk.api.district.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LegalDongCodeTest {

    @Test
    fun `올바른 10자리 법정동 코드로 생성할 수 있다`() {
        val legalCode = "1129010100"
        
        val dongCode = LegalDongCode(legalCode)
        
        assertThat(dongCode.value).isEqualTo(legalCode)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "123", "12345678901", "abcd123456", "112901010a"])
    fun `잘못된 형식의 법정동 코드는 IllegalArgumentException을 발생시킨다`(invalidCode: String) {
        assertThatThrownBy { LegalDongCode(invalidCode) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("올바른 법정동 코드 형식이 아닙니다: $invalidCode")
    }
}