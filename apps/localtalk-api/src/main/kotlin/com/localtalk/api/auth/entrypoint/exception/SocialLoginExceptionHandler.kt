package com.localtalk.api.auth.entrypoint.exception

import com.localtalk.common.model.RestResponse
import com.localtalk.logging.common.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException

@RestControllerAdvice
class SocialLoginExceptionHandler {
    val log = logger()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Invalid argument: {}", e.message)
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, e.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Illegal state: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(RestResponse.error(HttpStatus.CONFLICT, e.message ?: "요청을 처리할 수 없는 상태입니다."))
    }

    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientResponseException(e: WebClientResponseException): ResponseEntity<RestResponse<Unit>> {
        log.warn("External API call failed: status={}, body={}", e.statusCode, e.responseBodyAsString, e)

        val errorMessage = when (e.statusCode) {
            HttpStatus.BAD_REQUEST -> "소셜 로그인 토큰이 유효하지 않습니다."
            HttpStatus.UNAUTHORIZED -> "소셜 로그인 토큰이 만료되었습니다."
            HttpStatus.FORBIDDEN -> "소셜 로그인 접근 권한이 없습니다."
            else -> "소셜 로그인 서비스에 일시적인 문제가 발생했습니다."
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, errorMessage))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<RestResponse<Unit>> {
        log.error("Unexpected error occurred", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."))
    }
}
