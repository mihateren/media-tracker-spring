package com.example.mediatracker.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class OpenApiConfig {

    @Bean
    fun mediaTrackerOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("MediaTracker API")
                    .version("v1")
                    .description("Сервис для трекинга фильмов и сериалов")

            )
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth")
            )
    }

}