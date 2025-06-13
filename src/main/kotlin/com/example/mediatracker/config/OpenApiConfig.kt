package com.example.mediatracker.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
            .addSecurityItem(SecurityRequirement().addList("BearerAuth"))
            .components(
                Components()
                    .addSecuritySchemes(
                        "BearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }

}