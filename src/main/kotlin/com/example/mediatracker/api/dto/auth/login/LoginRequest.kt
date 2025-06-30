package com.example.mediatracker.api.dto.auth.login

data class LoginRequest(
    val username: String,
    val password: String
) {
}