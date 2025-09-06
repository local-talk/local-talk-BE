package com.localtalk.api.file.entrypoint

import com.localtalk.api.file.domain.FileType
import com.localtalk.api.support.IntegrationTest
import com.localtalk.api.support.fixture.FileFixture
import com.localtalk.s3.config.S3TestFixtures
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters

class FileControllerTest : IntegrationTest() {

    @Autowired
    lateinit var s3TestFixtures: S3TestFixtures

    val testBucket = "test-bucket"

    @BeforeEach
    fun setUp() {
        s3TestFixtures.setupTestBucket(testBucket)
    }

    @AfterEach
    fun s3CleanUp() {
        s3TestFixtures.teardownTestBucket(testBucket)
    }

    private fun uploadFile(client: WebTestClient, file: MockMultipartFile, type: String): WebTestClient.ResponseSpec {
        val bodyBuilder = MultipartBodyBuilder()

        bodyBuilder.part("file", file.resource)
            .filename(file.originalFilename)
            .contentType(MediaType.parseMediaType(file.contentType!!))

        bodyBuilder.part("type", type)
            .contentType(MediaType.TEXT_PLAIN)

        return client.post()
            .uri("/api/v1/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
            .exchange()
    }

    @Nested
    inner class `파일 업로드 성공 케이스를 전달했을 때` {

        @Test
        fun `JPEG 이미지 파일이면 파일 URL을 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.jpg", "image/jpeg")

            val response = uploadFile(authenticatedClient, testFile, FileType.PROFILE.name)

            response
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("파일 업로드가 완료되었습니다")
                .jsonPath("$.data.id").isEqualTo(1L)
                .jsonPath("$.data.url").value<String> {
                    assertThat(it).startsWith("https://")
                    assertThat(it).contains("profiles/")
                }
        }

        @Test
        fun `PNG 이미지 파일이면 파일 URL을 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.png", "image/png")

            val response = uploadFile(authenticatedClient, testFile, "PROFILE")

            response
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.message").isEqualTo("파일 업로드가 완료되었습니다")
                .jsonPath("$.data.id").isEqualTo(1L)
                .jsonPath("$.data.url").value<String> {
                    assertThat(it).startsWith("https://")
                    assertThat(it).contains("profiles/")
                }
        }

    }

    @Nested
    inner class `잘못된 파일 타입을 전달했을 때` {

        @Test
        fun `지원하지 않는 FileType이면 400 에러를 반환한다`() {
            val invalidType = "INVALID_TYPE"
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.jpg", "image/jpeg")

            val response = uploadFile(authenticatedClient, testFile, invalidType)

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("지원하지 않는 파일 타입입니다: $invalidType. 가능한 값: ${FileType.entries.joinToString { it.name }}")
        }

        @Test
        fun `지원하지 않는 MIME 타입이면 400 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.pdf", "application/pdf")

            val response = uploadFile(authenticatedClient, testFile, "PROFILE")

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("지원하지 않는 파일 형식입니다")
        }

        @Test
        fun `텍스트 파일이면 400 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.txt", "text/plain")

            val response = uploadFile(authenticatedClient, testFile, "PROFILE")

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("지원하지 않는 파일 형식입니다")
        }
    }

    @Nested
    inner class `잘못된 파일을 전달했을 때` {

        @Test
        fun `빈 파일이면 400 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val emptyFile = FileFixture.createMockFile("empty-file.jpg", "image/jpeg", size = 0)

            val response = uploadFile(authenticatedClient, emptyFile, "PROFILE")

            response
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("파일이 비어있습니다")
        }

        @Test
        fun `대용량 파일이면 413 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val largeTestFile = FileFixture.createMockFile("large-test-file.jpg", "image/jpeg", size = 11L * 1024 * 1024)

            val response = uploadFile(authenticatedClient, largeTestFile, "PROFILE")

            response
                .expectStatus().is4xxClientError
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE.value())
                .jsonPath("$.message").isEqualTo("업로드 가능한 파일 크기를 초과했습니다.")
        }
    }

    @Nested
    inner class `잘못된 요청을 전달했을 때` {

        @Test
        fun `파일이 없으면 400 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()

            val multipartData = LinkedMultiValueMap<String, Any>()
            multipartData.add("type", "PROFILE")

            val response = authenticatedClient
                .post()
                .uri("/api/v1/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartData)
                .exchange()

            response
                .expectStatus().isBadRequest
        }

        @Test
        fun `파일 타입이 없으면 400 에러를 반환한다`() {
            val authenticatedClient = loginAsTemporaryMember()
            val testFile = FileFixture.createMockFile("test.jpg", "image/jpeg")

            val resource = object : ByteArrayResource(testFile.bytes) {
                override fun getFilename(): String = testFile.originalFilename ?: "unknown"
            }

            val multipartData = LinkedMultiValueMap<String, Any>()
            multipartData.add("file", resource)

            val response = authenticatedClient
                .post()
                .uri("/api/v1/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartData)
                .exchange()

            response
                .expectStatus().isBadRequest
        }

        @Test
        fun `인증되지 않은 사용자면 401 에러를 반환한다`() {
            val testFile = FileFixture.createMockFile("test.jpg", "image/jpeg")

            val response = uploadFile(webTestClient, testFile, "PROFILE")

            response
                .expectStatus().isUnauthorized
        }
    }

}
