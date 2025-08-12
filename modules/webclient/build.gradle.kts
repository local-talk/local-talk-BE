dependencies {
    api(libs.spring.boot.starter.webflux)
    api(libs.spring.boot.configuration.processor)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.reactor.test)
}
