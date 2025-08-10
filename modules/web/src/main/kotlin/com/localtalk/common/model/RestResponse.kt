package com.localtalk.common.model

import org.springframework.http.HttpStatus

data class RestResponse<T>(val code: Int, val data: T, val message: String) {
    companion object {
        fun <T> success(data: T, message: String, code: HttpStatus = HttpStatus.OK): RestResponse<T> =
            RestResponse(code.value(), data, message)

        fun success(message: String, code: HttpStatus = HttpStatus.OK): RestResponse<Unit> =
            RestResponse(code.value(), Unit, message)

        fun <T> error(code: HttpStatus, data: T, message: String): RestResponse<T> =
            RestResponse(code.value(), data, message)

        fun error(code: HttpStatus, message: String): RestResponse<Unit> =
            RestResponse(code.value(), Unit, message)
    }
}
