package com.localtalk.api.utils

import java.util.Base64

object JwtTestUtils {

    fun hasValidJwtStructure(token: String): Boolean {
        val parts = token.split(".")
        return parts.size == 3 && parts.all { it.isNotBlank() }
    }

    fun hasValidBase64Header(token: String): Boolean {
        val parts = token.split(".")
        if (parts.isEmpty()) return false

        return try {
            Base64.getUrlDecoder().decode(parts[0])
            true
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    fun hasValidBase64Payload(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size < 2) return false

        return try {
            Base64.getUrlDecoder().decode(parts[1])
            true
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    fun hasSignature(token: String): Boolean {
        val parts = token.split(".")
        return parts.size == 3 && parts[2].isNotBlank()
    }

    fun isValidJwtFormat(token: String): Boolean {
        return hasValidJwtStructure(token) &&
            hasValidBase64Header(token) &&
            hasValidBase64Payload(token) &&
            hasSignature(token)
    }
}
