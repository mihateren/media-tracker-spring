package com.example.mediatracker.domain.entity

import java.time.OffsetDateTime

data class User(
    var id: Long? = null,
    var username: String,
    var email: String? = null,
    var passwordHash : String? = null,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
)
