package com.example.mediatracker.domain.entity.impl

import com.example.mediatracker.domain.entity.BaseEntity
import java.time.Clock
import java.time.OffsetDateTime

data class UserProfile(
    var userId: Long,
    var enabled: Boolean = true,
    var avatarUrl: String = DEFAULT_AVATAR,
): BaseEntity() {
    companion object {
        const val DEFAULT_AVATAR =
            "https://i.pinimg.com/originals/e6/34/8d/e6348d6b02fe4aff4e316ee1c637bd29.png"
    }
}
