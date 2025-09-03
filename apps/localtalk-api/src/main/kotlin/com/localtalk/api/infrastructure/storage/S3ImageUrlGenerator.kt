package com.localtalk.api.infrastructure.storage

import com.localtalk.s3.config.S3Properties
import org.springframework.stereotype.Component

@Component
class S3ImageUrlGenerator(
    private val s3Properties: S3Properties
) {
    
    fun generateImageUrl(imageKey: String): String {
        return "https://${s3Properties.defaultBucket}.s3.${s3Properties.region}.amazonaws.com/$imageKey"
    }
}