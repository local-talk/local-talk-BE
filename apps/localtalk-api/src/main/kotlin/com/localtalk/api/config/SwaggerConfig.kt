package com.localtalk.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
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
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth"),
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요 (Bearer 제외)"),
                    ),
            )
            .addServersItem(
                Server()
                    .url("https://localtalk.site")
                    .description("운영 서버"),
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("로컬 개발 서버"),
            )
    }
}
