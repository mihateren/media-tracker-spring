package com.example.mediatracker.api.dto.auth.registration

data class RegistrationRequest(
    val username: String,
    val password: String,
    val email: String
) {
}