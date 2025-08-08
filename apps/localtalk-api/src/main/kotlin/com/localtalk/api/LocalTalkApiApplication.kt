package com.localtalk.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LocalTalkApiApplication

fun main(args: Array<String>) {
    runApplication<LocalTalkApiApplication>(*args)
}