package com.localtalk.s3.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "storage.s3")
data class S3Properties (
    var defaultBucket: String = "localtalk-storage",
    var region: String = "ap-northeast-2"
)
