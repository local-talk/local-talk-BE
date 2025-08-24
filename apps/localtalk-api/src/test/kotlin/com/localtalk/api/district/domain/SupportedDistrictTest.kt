package com.localtalk.api.district.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SupportedDistrictTest {

    @Nested
    inner class `getByPathName을 호출할 때` {

        @Test
        fun `지원하는 구를 전달하면 해당하는 구의 동을 반환한다`() {
            val want = SupportedDistrict.SEONGBUK
            val got = SupportedDistrict.getByPathName("seongbuk")

            assertThat(got).isEqualTo(want)
            assertThat(got.pathName).isEqualTo("seongbuk")
            assertThat(got.displayName).isEqualTo("성북구")
        }

        @ParameterizedTest
        @ValueSource(strings = ["gangnam", "seocho", "songpa", "unknown"])
        fun `지원하지 않는 구들은 IllegalArgumentException을 발생시킨다`(pathName: String) {
            assertThatThrownBy { SupportedDistrict.getByPathName(pathName) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("지원하지 않는 구입니다: $pathName")
        }
    }
}
