package com.localtalk.api.member.domain

enum class Gender(
    val description: String,
) {
    MALE("남성"),
    FEMALE("여성"),
    NONE("선택 안함");
    
    companion object {
        fun fromString(value: String): Gender = 
            entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("지원하지 않는 성별입니다: $value. 가능한 값: ${entries.joinToString { it.name }}")
    }
}
