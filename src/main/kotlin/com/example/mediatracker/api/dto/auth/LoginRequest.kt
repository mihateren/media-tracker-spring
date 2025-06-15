package com.example.mediatracker.api.dto.auth

data class LoginRequest(
    val username: String,
    val password: String
) {
}