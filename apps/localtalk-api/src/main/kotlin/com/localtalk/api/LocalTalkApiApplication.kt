package com.localtalk.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.localtalk"])
class LocalTalkApiApplication

fun main(args: Array<String>) {
    runApplication<LocalTalkApiApplication>(*args)
}