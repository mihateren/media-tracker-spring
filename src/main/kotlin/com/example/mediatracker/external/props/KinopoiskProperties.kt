package com.example.mediatracker.external.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "hosts.kinopoisk")
class KinopoiskProperties @ConstructorBinding constructor(
    val url: String,
    val apiKey: String
)