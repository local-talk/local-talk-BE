package com.localtalk.s3.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@TestConfiguration
class LocalStackS3Config {

    companion object {
        val localStackContainer: LocalStackContainer = LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.8.1")
        ).withServices(Service.S3)

        init {
            localStackContainer.start()
        }
    }

    @Bean
    fun localStackContainer(): LocalStackContainer = localStackContainer
    
    @Bean
    @Primary
    fun testS3Client(): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(localStackContainer.endpoint.toString()))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(localStackContainer.accessKey, localStackContainer.secretKey)
                )
            )
            .region(Region.of(localStackContainer.region))
            .forcePathStyle(true)
            .build()
    }
}
