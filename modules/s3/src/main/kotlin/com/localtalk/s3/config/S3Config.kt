package com.localtalk.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3Config {

    @Bean
    @Profile("local")
    fun localStackS3Client(
        @Value("\${spring.cloud.aws.s3.endpoint:http://localhost:4566}") endpoint: String,
        @Value("\${storage.s3.region:us-east-1}") region: String
    ): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test")
                )
            )
            .forcePathStyle(true)
            .build()
    }
}
