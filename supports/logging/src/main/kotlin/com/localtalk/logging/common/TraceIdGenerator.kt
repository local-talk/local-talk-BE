package com.localtalk.logging.common

import java.util.*

object TraceIdGenerator {
    fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16)
    }
}