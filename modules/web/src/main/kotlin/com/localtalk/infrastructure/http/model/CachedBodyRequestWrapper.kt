package com.localtalk.infrastructure.http.model

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.ByteArrayInputStream

class CachedBodyRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray = request.inputStream.readAllBytes()

    override fun getInputStream(): ServletInputStream = CachedServletInputStream(ByteArrayInputStream(cachedBody))

    fun getTruncatedBody(maxLength: Int = 1000): String {
        if (cachedBody.isEmpty()) return ""

        val body = String(cachedBody, Charsets.UTF_8)
        return if (body.length > maxLength) {
            "${body.take(maxLength)}... (${body.length} chars)"
        } else body
    }

    fun hasBody(): Boolean = cachedBody.isNotEmpty()
}

private class CachedServletInputStream(private val inputStream: ByteArrayInputStream) : ServletInputStream() {

    override fun read(): Int = inputStream.read()

    override fun isFinished(): Boolean = inputStream.available() == 0

    override fun isReady(): Boolean = true

    override fun setReadListener(readListener: ReadListener?) {
        readListener?.onDataAvailable()
    }
}