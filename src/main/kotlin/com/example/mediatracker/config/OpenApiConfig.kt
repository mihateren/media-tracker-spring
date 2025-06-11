package com.example.mediatracker.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun mediaTrackerOpenApi(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("MediaTracker API")
                .version("v1")
                .description("Сервис для трекинга фильмов и сериалов")

        )
    }

}