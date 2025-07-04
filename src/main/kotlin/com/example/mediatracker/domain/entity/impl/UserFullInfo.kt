package com.example.mediatracker.domain.entity.impl

import com.example.mediatracker.domain.entity.BaseEntity
import java.time.OffsetDateTime

data class UserFullInfo(
    val id: Long,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val enabled: Boolean,
): BaseEntity()