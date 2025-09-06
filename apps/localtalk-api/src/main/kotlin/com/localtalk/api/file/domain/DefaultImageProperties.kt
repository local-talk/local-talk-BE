package com.localtalk.api.file.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "default-image")
data class DefaultImageProperties(
    var profile: String = "https://localtalk-bucket.s3.ap-northeast-2.amazonaws.com/default/profile.png",
)
