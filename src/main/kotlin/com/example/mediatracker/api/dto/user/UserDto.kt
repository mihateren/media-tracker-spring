package com.example.mediatracker.api.dto.user

import java.time.OffsetDateTime

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val enabled: Boolean,
) {
}