package com.localtalk.api.common.application

interface ImageUrlGenerator {
    fun generateImageUrl(imageKey: String): String
}
