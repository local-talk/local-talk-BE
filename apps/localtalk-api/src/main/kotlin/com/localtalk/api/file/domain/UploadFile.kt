package com.localtalk.api.file.domain

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

private val ALLOWED_FILE_TYPE = setOf(
    "image/jpeg", "image/jpg", "image/png",
)

data class UploadFile(
    val file: MultipartFile,
    val type: FileType,
) {
    init {
        require(!file.isEmpty) { "파일이 비어있습니다" }
        require(file.size <= type.size) { "파일 크기는 최대 ${type.size / MB}MB를 초과할 수 없습니다" }
        require(file.contentType in ALLOWED_FILE_TYPE) { "지원하지 않는 파일 형식입니다" }
    }

    val uploadName: String by lazy {
        val ext = originFileName.substringAfterLast('.', "")
        if (ext.isNotEmpty()) "${UUID.randomUUID()}.$ext"
        else "${UUID.randomUUID()}.${file.contentType!!.substringAfterLast('/', "")}"
    }

    val path: String
        get() = type.path

    val originFileName: String
        get() = file.originalFilename ?: "unknown"

}
