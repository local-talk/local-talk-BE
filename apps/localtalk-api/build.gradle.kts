plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation(project(":modules:jpa"))
    implementation(project(":modules:web"))
    implementation(project(":modules:webclient"))
    implementation(project(":supports:logging"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}
