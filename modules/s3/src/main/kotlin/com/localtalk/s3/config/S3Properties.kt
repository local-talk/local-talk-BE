package com.localtalk.s3.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "storage.s3")
data class S3Properties(
    val defaultBucket: String = "localtalk-storage",
    val region: String = "ap-northeast-2"
)