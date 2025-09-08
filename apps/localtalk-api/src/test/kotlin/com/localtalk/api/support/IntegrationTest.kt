package com.localtalk.api.support

import com.fasterxml.jackson.databind.JsonNode
import com.localtalk.config.MysqlTestContainerConfig
import com.localtalk.s3.config.LocalStackS3Config
import com.localtalk.utils.JpaDatabaseCleaner
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(MysqlTestContainerConfig::class, LocalStackS3Config::class, KakaoApiMockServer::class, TestClockConfig::class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class IntegrationTest {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var databaseCleaner: JpaDatabaseCleaner

    @Autowired
    protected lateinit var clock: MutableClock

    @AfterEach
    fun tearDown() {
        databaseCleaner.truncateAllTables()
        clock.now()
    }

    fun loginAsTemporaryMember(): WebTestClient {
        val validAccessToken = "valid_kakao_access_token"

        KakaoApiMockServer.enqueueSuccessResponse(id = 123456789L, expiresIn = 3600, appId = 12345)

        val token = webTestClient.post()
            .uri("/api/v1/social-logins/kakao")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"access_token":"$validAccessToken"}""")
            .exchange()
            .expectStatus().isOk
            .returnResult<JsonNode>()
            .responseBody
            .blockFirst()!!["data"]["access_token"].asText()

        return webTestClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .build()
    }
}
