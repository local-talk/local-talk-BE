package com.localtalk.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class JacksonUtilsTest {

    @Nested
    @DisplayName("toPrettyJson")
    inner class ToPrettyJsonTest {

        val objectMapper = ObjectMapper()

        @Test
        @DisplayName("JSON 문자열이면 포맷된 JSON을 반환합니다.")
        fun shouldReturnPrettyJsonWhenValidJson() {
            val jsonString = """{"name":"홍길동","age":30}"""

            val result = objectMapper.toPrettyJson(jsonString)

            assertThat(result).isEqualTo(
                """
               {
                 "name" : "홍길동",
                 "age" : 30
               }
                """.trimIndent(),
            )
        }

        @ParameterizedTest
        @DisplayName("JSON 형식이 아니면 원본 문자열을 반환합니다.")
        @ValueSource(strings = ["잘못된 JSON", "{잘못된형식}", "일반 텍스트"])
        fun shouldReturnOriginalStringWhenInvalidJson(invalidJson: String) {
            val result = objectMapper.toPrettyJson(invalidJson)

            assertThat(result).isEqualTo(invalidJson)
        }
    }
}
