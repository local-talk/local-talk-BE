package com.localtalk.api.member.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GenderTest {

    @ParameterizedTest
    @ValueSource(strings = ["MALE", "male", "Male"])
    fun `MALE 관련 문자열을 전달하면 MALE Gender를 반환한다`(input: String) {
        val result = Gender.fromString(input)

        assertThat(result).isEqualTo(Gender.MALE)
    }

    @ParameterizedTest
    @ValueSource(strings = ["FEMALE", "female", "Female"])
    fun `FEMALE 관련 문자열을 전달하면 FEMALE Gender를 반환한다`(input: String) {
        val result = Gender.fromString(input)

        assertThat(result).isEqualTo(Gender.FEMALE)
    }

    @ParameterizedTest
    @ValueSource(strings = ["NONE", "none", "None"])
    fun `NONE 관련 문자열을 전달하면 NONE Gender를 반환한다`(input: String) {
        val result = Gender.fromString(input)

        assertThat(result).isEqualTo(Gender.NONE)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "UNKNOWN", "other", ""])
    fun `지원하지 않는 문자열을 전달하면 예외를 발생시킨다`(input: String) {
        assertThatThrownBy { Gender.fromString(input) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("지원하지 않는 성별입니다: $input. 가능한 값: MALE, FEMALE, NONE")
    }

}
