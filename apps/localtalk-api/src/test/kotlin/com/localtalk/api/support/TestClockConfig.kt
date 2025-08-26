package com.localtalk.api.support

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.time.Instant

@TestConfiguration
class TestClockConfig {

    @Bean
    @Primary
    fun testClock(): MutableClock = MutableClock(Instant.now())
}
