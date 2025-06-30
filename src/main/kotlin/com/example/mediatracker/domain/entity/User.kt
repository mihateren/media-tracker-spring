package com.example.mediatracker.domain.entity

data class User(
    val id: Long = 0L,
    val username: String,
    val email: String,
    val passwordHash : String,
)
