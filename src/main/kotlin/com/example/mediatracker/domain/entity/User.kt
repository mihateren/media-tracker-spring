package com.example.mediatracker.domain.entity

data class User(
    var id: Long? = null,
    var username: String,
    var email: String? = null,
    var passwordHash : String? = null,
)
