package com.localtalk.s3.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class LocalStackS3Config {

    companion object {
        val localStackContainer: LocalStackContainer = LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.8.1")
        ).withServices(Service.S3)

        init {
            localStackContainer.start()

            System.setProperty("spring.cloud.aws.s3.endpoint", localStackContainer.endpoint.toString())
            System.setProperty("spring.cloud.aws.credentials.access-key", localStackContainer.accessKey)
            System.setProperty("spring.cloud.aws.credentials.secret-key", localStackContainer.secretKey)
            System.setProperty("spring.cloud.aws.region.static", localStackContainer.region)
            System.setProperty("storage.s3.region", localStackContainer.region)
        }
    }

    @Bean
    fun localStackContainer(): LocalStackContainer = localStackContainer
}
