plugins {
    `java-test-fixtures`
}

dependencies {
    api(libs.spring.cloud.aws.starter.s3)
    api(libs.spring.boot.configuration.processor)
    // multipart file 사용을 위해 추가
    api("org.springframework:spring-web")

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.localstack)

    testFixturesImplementation(libs.spring.boot.starter.test)
    testFixturesImplementation(libs.spring.test)
    testFixturesImplementation(libs.spring.cloud.aws.testcontainers)
    testFixturesImplementation(libs.testcontainers.localstack)
}
