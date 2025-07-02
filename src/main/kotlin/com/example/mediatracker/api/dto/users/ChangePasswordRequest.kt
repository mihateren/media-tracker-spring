package com.example.mediatracker.api.dto.users

class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
) {
}