package com.localtalk.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnvTestController {
    @Value("\${MYSQL_HOST:NOT_SET}")
    lateinit var mysqlHost: String

    @GetMapping("/env-test")
    fun envTest() = "MYSQL_HOST=$mysqlHost"
}
