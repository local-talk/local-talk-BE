plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.mysql:mysql-connector-j")
}