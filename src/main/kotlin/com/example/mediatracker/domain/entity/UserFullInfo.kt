package com.example.mediatracker.domain.entity

import java.time.OffsetDateTime

data class UserFullInfo(
    val id: Long,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val enabled: Boolean,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
)