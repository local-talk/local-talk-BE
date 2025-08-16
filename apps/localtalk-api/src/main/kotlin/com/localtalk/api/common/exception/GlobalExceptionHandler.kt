package com.localtalk.api.common.exception

import com.localtalk.common.model.RestResponse
import com.localtalk.logging.common.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Invalid argument: {}", e.message)
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, e.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<RestResponse<Unit>> {
        log.error("Unexpected error occurred", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."))
    }
}
