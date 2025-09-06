package com.localtalk.api.support.fixture

import org.springframework.mock.web.MockMultipartFile

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
}
