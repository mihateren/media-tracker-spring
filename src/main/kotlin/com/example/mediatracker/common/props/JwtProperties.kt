package com.example.mediatracker.common.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val expiration: Expiration
) {
    data class Expiration(
        val accessMin: Long,
        val refreshDays: Long
    )
}
