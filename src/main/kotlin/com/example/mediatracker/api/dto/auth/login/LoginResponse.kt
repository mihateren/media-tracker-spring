package com.example.mediatracker.api.dto.auth.login

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
) {
}