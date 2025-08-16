package com.localtalk.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Local Talk API")
                    .description("Local Talk 백엔드 API 문서")
                    .version("1.0.0"),
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("로컬 개발 서버"),
            )
    }
}
