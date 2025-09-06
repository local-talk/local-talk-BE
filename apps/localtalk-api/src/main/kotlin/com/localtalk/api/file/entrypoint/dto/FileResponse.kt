package com.localtalk.api.file.entrypoint.dto

import java.time.LocalDateTime

data class FileResponse(
    val fileId: String,
    val url: String,
    val originalFileName: String,
    val storedFileName: String,
    val fileSize: Long,
    val contentType: String,
    val directory: String,
    val isTemporary: Boolean,
    val expiresAt: LocalDateTime?,
    val createdAt: LocalDateTime,
)