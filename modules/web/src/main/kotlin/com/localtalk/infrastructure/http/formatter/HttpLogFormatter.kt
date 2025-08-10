package com.localtalk.infrastructure.http.formatter

import com.fasterxml.jackson.databind.ObjectMapper
import com.localtalk.common.util.getTruncatedBody
import com.localtalk.common.util.hasBody
import com.localtalk.common.util.toPrettyJson
import com.localtalk.infrastructure.http.model.CachedBodyRequestWrapper
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class HttpLogFormatter(private val objectMapper: ObjectMapper) {

    fun formatRequest(request: CachedBodyRequestWrapper): String {
        val queryString = request.queryString?.let { "?$it" } ?: ""
        val bodyInfo = if (request.hasBody()) {
            "\n${objectMapper.toPrettyJson(request.getTruncatedBody())}"
        } else {
            ""
        }

        return "📥 [HTTP REQUEST]\n" +
            "  Method    : ${request.method}\n" +
            "  URL       : ${request.requestURI}$queryString$bodyInfo"
    }

    fun formatResponse(response: ContentCachingResponseWrapper, duration: Long): String {
        val status = response.status
        val bodyInfo = if (response.hasBody()) {
            "\n${objectMapper.toPrettyJson(response.getTruncatedBody())}"
        } else {
            ""
        }

        return "📤 [HTTP RESPONSE]\n" +
            "  Status    : $status\n" +
            "  Duration  : ${duration}ms$bodyInfo"
    }
}
