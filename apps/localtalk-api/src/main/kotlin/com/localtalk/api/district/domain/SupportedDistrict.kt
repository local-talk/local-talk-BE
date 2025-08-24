package com.localtalk.api.district.domain

enum class SupportedDistrict(val pathName: String, val displayName: String) {
    SEONGBUK("seongbuk", "성북구");

    companion object {
        fun getByPathName(pathName: String): SupportedDistrict {
            return entries.find { it.pathName == pathName }
                ?: throw IllegalArgumentException("지원하지 않는 구입니다: $pathName")
        }
    }
}
