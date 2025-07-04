package com.example.mediatracker.domain.entity

import java.time.OffsetDateTime

data class UserProfile(
    var userId: Long,
    var enabled: Boolean = true,
    var avatarUrl: String = DEFAULT_AVATAR,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
){
    companion object {
        const val DEFAULT_AVATAR =
            "https://i.pinimg.com/originals/e6/34/8d/e6348d6b02fe4aff4e316ee1c637bd29.png"
    }
}
