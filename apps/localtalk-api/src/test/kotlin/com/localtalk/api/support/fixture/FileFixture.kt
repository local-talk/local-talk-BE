package com.localtalk.api.support.fixture

import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.MultiValueMap

object FileFixture {

    fun createMockFile(
        name: String = "test.png",
        contentType: String = "image/png",
        size: Long = 1024,
    ) = MockMultipartFile(
        "file",
        name,
        contentType,
        ByteArray(size.toInt()),
    )

    fun createMultipartData(
        file: MockMultipartFile,
        type: String
    ): MultiValueMap<String, HttpEntity<*>> {
        val bodyBuilder = MultipartBodyBuilder()

        bodyBuilder.part("file", file.resource)
            .filename(file.originalFilename)
            .contentType(MediaType.parseMediaType(file.contentType!!))

        bodyBuilder.part("type", type)
            .contentType(MediaType.TEXT_PLAIN)

        return bodyBuilder.build()
    }
}
