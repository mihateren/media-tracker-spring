package com.example.mediatracker.common.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val authSecret: String,
    val inviteSecret: String,
    val expiration: Expiration
) {
    data class Expiration(
        val accessMin: Long,
        val refreshDays: Long,
        val inviteDays: Long
    )
}
