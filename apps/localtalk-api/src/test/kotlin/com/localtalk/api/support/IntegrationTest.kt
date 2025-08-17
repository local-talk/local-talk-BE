package com.localtalk.api.support

import com.localtalk.api.config.KakaoApiMockServer
import com.localtalk.config.MysqlTestContainerConfig
import com.localtalk.utils.JpaDatabaseCleaner
import com.localtalk.webclient.config.WebTestClientConfig
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(MysqlTestContainerConfig::class, KakaoApiMockServer::class, WebTestClientConfig::class)
@ActiveProfiles("test")
abstract class IntegrationTest {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var databaseCleaner: JpaDatabaseCleaner

    @AfterEach
    fun tearDown() {
        databaseCleaner.truncateAllTables()
    }
}
