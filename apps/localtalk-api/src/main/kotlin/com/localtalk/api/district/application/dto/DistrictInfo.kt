package com.localtalk.api.district.application.dto

data class DistrictInfo(
    val name: String,
    val dongs: List<DongInfo>
)