package com.localtalk.webclient

import com.localtalk.webclient.config.WebClientProperties
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
@EnableConfigurationProperties(WebClientProperties::class)
class WebClientConfig(
    private val properties: WebClientProperties,
) {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        val httpClient = createHttpClient()

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(properties.maxInMemorySize)
            }
    }

    @Bean
    fun defaultWebClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder.build()
    }

    private fun createHttpClient(): HttpClient {
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.connectTimeout.toMillis().toInt())
            .responseTimeout(properties.responseTimeout)
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(properties.readTimeout.toSeconds(), TimeUnit.SECONDS))
                    .addHandlerLast(WriteTimeoutHandler(properties.writeTimeout.toSeconds(), TimeUnit.SECONDS))
            }
    }
}
