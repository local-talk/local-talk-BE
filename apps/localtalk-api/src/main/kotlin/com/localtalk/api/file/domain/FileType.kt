package com.localtalk.api.file.domain

const val MB = 1024 * 1024

enum class FileType(val path: String, val size: Int) {
    PROFILE("profiles", 10 * MB);

    companion object {
        fun fromString(value: String?): FileType =
            entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("지원하지 않는 파일 타입입니다: $value. 가능한 값: ${entries.joinToString { it.name }}")
    }
}
