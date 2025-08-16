plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation(project(":modules:jpa"))
    implementation(project(":modules:web"))
    implementation(project(":modules:webclient"))
    implementation(project(":supports:logging"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // JWT
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation(libs.mockk)
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    testImplementation(testFixtures(project(":modules:jpa")))
}
