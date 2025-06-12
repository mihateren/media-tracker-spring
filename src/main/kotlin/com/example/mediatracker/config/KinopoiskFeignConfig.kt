package com.example.mediatracker.config

import com.example.mediatracker.external.KinopoiskClient
import com.example.mediatracker.external.props.KinopoiskProperties
import feign.Logger
import feign.RequestInterceptor
import feign.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KinopoiskFeignConfig (
    val props: KinopoiskProperties,
) {

    @Bean
    fun authInterceptor(): RequestInterceptor = RequestInterceptor { template ->
        template.header("X-API-KEY", props.apiKey)
    }

    @Bean
    fun feignLoggerLevel(): feign.Logger.Level = feign.Logger.Level.FULL

    @Bean
    fun feignLogger(): Logger = Slf4jLogger(KinopoiskClient::class.java)

}