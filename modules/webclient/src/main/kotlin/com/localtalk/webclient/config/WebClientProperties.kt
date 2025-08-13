package com.localtalk.webclient.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "client.webclient")
data class WebClientProperties(
    val connectTimeout: Duration = Duration.ofSeconds(30),
    val responseTimeout: Duration = Duration.ofSeconds(30),
    val readTimeout: Duration = Duration.ofSeconds(30),
    val writeTimeout: Duration = Duration.ofSeconds(30),
    val maxInMemorySize: Int = 1024 * 1024 * 10, // 10MB
    val maxConnections: Int = 500,
    val pendingAcquireTimeout: Duration = Duration.ofSeconds(60),
)
