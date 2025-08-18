package com.localtalk.webclient.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

@Configuration
class WebTestClientConfig {

    @Bean
    fun webTestClient(context: WebApplicationContext): WebTestClient =
        MockMvcWebTestClient.bindToApplicationContext(context).build()
}
