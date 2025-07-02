package com.example.mediatracker.api.dto.auth

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
) {
}