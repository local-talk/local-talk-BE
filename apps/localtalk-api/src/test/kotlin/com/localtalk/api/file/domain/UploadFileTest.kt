package com.localtalk.api.file.domain

import com.localtalk.api.support.fixture.FileFixture.createMockFile
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class UploadFileTest {

    @Nested
    inner class `업로드 파일을 생성할 때` {
        @Test
        fun `파일이 비어있으면 예외가 발생합니다`() {
            val mockFile = createMockFile(size = 0)
            val type = FileType.PROFILE

            assertThatThrownBy { UploadFile(mockFile, type) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("파일이 비어있습니다")
        }

        @ParameterizedTest
        @EnumSource(FileType::class)
        fun `파일 크기가 파일 유형별 최대 크기를 초과하면 예외가 발생합니다`(type: FileType) {
            val maxSize = type.size + 1
            val mockFile = createMockFile(size = maxSize.toLong())

            assertThatThrownBy { UploadFile(mockFile, type) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("파일 크기는 최대 ${type.size / MB}MB를 초과할 수 없습니다")
        }

        @Test
        fun `지원히지 않는 형식이면 예외가 발생합니다`() {
            val mockFile = createMockFile(name = "test.txt", contentType = "text/plain")
            val type = FileType.PROFILE

            assertThatThrownBy { UploadFile(mockFile, type) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("지원하지 않는 파일 형식입니다")
        }

        @Nested
        inner class `업로드 파일의 이름을 생성할 때` {
            private val uuidPngRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.png$")

            @Test
            fun `UUID + 확장자로 업로드 파일 이름을 생성한다`() {
                val mockFile = createMockFile(name = "test.png", contentType = "image/png")

                val uploadFile = UploadFile(mockFile, FileType.PROFILE)
                val uploadName = uploadFile.uploadName

                assertThat(uploadName).matches(uuidPngRegex.toPattern())
            }

            @Test
            fun `확장자가 없는 파일명인 경우 UUID + Content Type의 서브타입으로 업로드 파일 이름을 생성한다`() {
                val mockFile = createMockFile(name = "test", contentType = "image/png")

                val uploadFile = UploadFile(mockFile, FileType.PROFILE)

                val uploadName = uploadFile.uploadName

                assertThat(uploadName).matches(uuidPngRegex.toPattern())
            }

        }
    }
}
