package com.example.mediatracker.domain.entity.user

import java.time.OffsetDateTime

data class UserFullInfo(
    val id: Long,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val enabled: Boolean,
) {
}