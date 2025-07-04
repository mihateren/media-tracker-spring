package com.example.mediatracker.domain.entity.user

data class User(
    var id: Long? = null,
    var username: String,
    var email: String? = null,
    var passwordHash : String? = null,
)
