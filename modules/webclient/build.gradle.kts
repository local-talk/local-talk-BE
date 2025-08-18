plugins {
    `java-test-fixtures`
}

dependencies {
    api(libs.spring.boot.starter.webflux)
    api(libs.spring.boot.configuration.processor)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.reactor.test)

    testFixturesImplementation(libs.spring.boot.starter.test)
    testFixturesImplementation(libs.spring.test)
}
