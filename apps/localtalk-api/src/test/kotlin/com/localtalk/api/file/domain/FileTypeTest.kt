package com.localtalk.api.file.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class FileTypeTest {

    @Nested
    inner class `파일 타입을 전달할 때 해당 문자열에 맞는 FileType을 반환한다` {

        @ParameterizedTest
        @EnumSource(FileType::class)
        fun `모든 FileType에 대해 fromString이 올바른 FileType을 반환한다`(
            fileType: FileType,
        ) {
            val type = fileType.name

            val got = FileType.fromString(type)

            assertThat(got).isEqualTo(fileType)
        }

        @Test
        fun `지원하지 않는 파일 타입이 들어오면 IllegalArgumentException을 던진다`() {
            val invalidType = "INVALID_TYPE"

            assertThatThrownBy { FileType.fromString(invalidType) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("지원하지 않는 파일 타입입니다")
        }
    }

}
