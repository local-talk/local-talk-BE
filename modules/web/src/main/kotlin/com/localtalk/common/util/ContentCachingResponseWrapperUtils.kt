package com.localtalk.common.util

import org.springframework.web.util.ContentCachingResponseWrapper

internal fun ContentCachingResponseWrapper.hasBody(): Boolean = contentAsByteArray.isNotEmpty()

internal fun ContentCachingResponseWrapper.getTruncatedBody(maxLength: Int = 1000): String {
    require(maxLength >= 0) { "maxLength must be non-negative" }
    if (!hasBody()) return ""

    val body = String(contentAsByteArray, Charsets.UTF_8)
    return if (body.length > maxLength) {
        "${body.take(maxLength)}... (${body.length} chars)"
    } else {
        body
    }
}
