package com.example.mediatracker.domain.entity

import java.time.Clock
import java.time.OffsetDateTime

data class UserProfile(
    val userId: Long,
    val enabled: Boolean = true,
    val createdAt: OffsetDateTime = OffsetDateTime.now(Clock.systemUTC()),
    val updatedAt: OffsetDateTime? = null,
    val avatarUrl: String = DEFAULT_AVATAR,
) {
    companion object {
        const val DEFAULT_AVATAR =
            "https://i.pinimg.com/originals/e6/34/8d/e6348d6b02fe4aff4e316ee1c637bd29.png"
    }
}
