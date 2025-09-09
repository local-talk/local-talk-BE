package com.localtalk.api.common.exception

import com.localtalk.common.model.RestResponse
import com.localtalk.logging.common.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.ErrorResponseException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.server.MissingRequestValueException
import org.springframework.web.server.NotAcceptableStatusException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerErrorException
import org.springframework.web.server.ServerWebInputException
import org.springframework.web.server.UnsatisfiedRequestParameterException
import org.springframework.web.server.UnsupportedMediaTypeStatusException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(
        MethodNotAllowedException::class,
        NotAcceptableStatusException::class,
        UnsupportedMediaTypeStatusException::class,
        MissingRequestValueException::class,
        UnsatisfiedRequestParameterException::class,
        WebExchangeBindException::class,
        HandlerMethodValidationException::class,
        ServerWebInputException::class,
        ServerErrorException::class,
        ResponseStatusException::class,
        ErrorResponseException::class,
        MethodValidationException::class,
        HttpRequestMethodNotSupportedException::class,
        HttpMediaTypeNotSupportedException::class,
    )
    fun handleRestApiRequestException(e: Exception): ResponseEntity<RestResponse<Unit>> {
        log.debug("Request error occurred", e)
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, e.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun handleMissingServletRequestPartException(e: MissingServletRequestPartException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Missing request part", e)
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다: ${e.requestPartName}"))
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Missing request parameter", e)
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다: ${e.parameterName}"))
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Max upload size exceeded", e)
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(RestResponse.error(HttpStatus.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Resource not found", e)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(RestResponse.error(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<RestResponse<Unit>> {
        log.debug("Message not readable", e)
        val message = when (val cause = e.rootCause) {
            is IllegalArgumentException -> cause.message ?: "잘못된 요청입니다."
            else -> "잘못된 요청 형식입니다."
        }
        return ResponseEntity
            .badRequest()
            .body(RestResponse.error(HttpStatus.BAD_REQUEST, message))
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleIllegalArgumentException(e: Exception): ResponseEntity<RestResponse<Unit>> {
        log.debug("Invalid argument", e)
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
