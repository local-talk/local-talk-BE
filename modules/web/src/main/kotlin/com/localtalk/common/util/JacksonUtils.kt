package com.localtalk.common.util

import com.fasterxml.jackson.databind.ObjectMapper

internal fun ObjectMapper.toPrettyJson(raw: String): String = try {
    writerWithDefaultPrettyPrinter().writeValueAsString(readTree(raw))
} catch (_: Exception) {
    raw
}
