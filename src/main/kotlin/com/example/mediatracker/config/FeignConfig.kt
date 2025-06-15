package com.example.mediatracker.config

import com.example.mediatracker.external.props.KinopoiskProperties
import com.example.mediatracker.logging.FeignLoggingInterceptor
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FeignConfig (
    val props: KinopoiskProperties,
) {

    // нужно для интерцептора логирования (отключаем дефолтное логгирование)
    @Bean
    fun feignLoggerLevel(): feign.Logger.Level = feign.Logger.Level.NONE

    @Bean
    fun feignLoggingInterceptor() = FeignLoggingInterceptor()

    @Bean
    fun feignOkHttpClient(loggingInterceptor: FeignLoggingInterceptor): feign.Client {
        val httpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return feign.okhttp.OkHttpClient(httpClient)
    }

    @Bean
    fun authInterceptor(): RequestInterceptor = RequestInterceptor { template ->
        template.header("X-API-KEY", props.apiKey)
    }

    @Bean
    fun feignMethodHeader(): RequestInterceptor = RequestInterceptor { template ->
        template.header("X-FEIGN-METHOD", template.methodMetadata().configKey())
    }

}