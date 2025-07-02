package com.example.mediatracker.api.dto.users

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val avatarUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val enabled: Boolean,
) {
}