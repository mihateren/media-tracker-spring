package com.example.mediatracker.config

import com.example.mediatracker.external.props.KinopoiskProperties
import feign.RequestInterceptor
import io.github.oshai.kotlinlogging.KLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KinopoiskFeignConfig (
    val props: KinopoiskProperties,
) {

    @Bean
    fun authInterceptor(): RequestInterceptor = RequestInterceptor { template ->
        template.header("Authorization", "X-API-KEY ", props.apiKey)
    }

    @Bean
    fun feignLoggerLevel(): feign.Logger.Level = feign.Logger.Level.FULL
}