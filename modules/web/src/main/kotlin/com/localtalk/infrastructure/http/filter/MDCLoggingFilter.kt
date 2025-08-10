package com.localtalk.infrastructure.http.filter

import com.localtalk.infrastructure.http.formatter.HttpLogFormatter
import com.localtalk.infrastructure.http.model.CachedBodyRequestWrapper
import com.localtalk.logging.common.MDCUtils
import com.localtalk.logging.common.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter(private val logFormatter: HttpLogFormatter) : OncePerRequestFilter() {

    private val log = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val startTime = System.currentTimeMillis()

        val wrappedRequest = CachedBodyRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        MDCUtils.withTrace {
            log.info(logFormatter.formatRequest(wrappedRequest))

            filterChain.doFilter(wrappedRequest, wrappedResponse)

            val duration = System.currentTimeMillis() - startTime
            log.info(logFormatter.formatResponse(wrappedResponse, duration))

            wrappedResponse.copyBodyToResponse()
        }
    }
}
