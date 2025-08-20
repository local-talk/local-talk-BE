plugins {
    `java-test-fixtures`
}

dependencies {
    api(libs.spring.cloud.aws.starter.s3)
    api(libs.spring.boot.configuration.processor)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.localstack)
    
    testFixturesImplementation(libs.spring.boot.starter.test)
    testFixturesImplementation(libs.spring.test)
    testFixturesImplementation(libs.spring.cloud.aws.testcontainers)
    testFixturesImplementation(libs.testcontainers.localstack)
}