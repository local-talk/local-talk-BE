package com.localtalk.api.member.application.dto

import org.springframework.web.multipart.MultipartFile

data class ProfileImageCommand(
    val profileImage: MultipartFile?,
) {
    fun hasImage(): Boolean = profileImage != null
    fun isEmpty(): Boolean = profileImage == null
}