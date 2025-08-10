package com.localtalk.logging.common

import org.slf4j.MDC

object MDCUtils {
    inline fun <T> withTrace(
        traceId: String = TraceIdGenerator.generate(),
        block: () -> T,
    ): T = try {
        MDC.put(MDCKeys.TRACE_ID, traceId)
        block()
    } finally {
        MDC.clear()
    }
}
