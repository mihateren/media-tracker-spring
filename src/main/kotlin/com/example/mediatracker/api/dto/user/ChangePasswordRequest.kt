package com.example.mediatracker.api.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class ChangePasswordRequest(

    @field:NotBlank
    val oldPassword: String,

    @field:Schema(
        description = "Пароль (мин. 8 символов, буквы и цифры)",
        example = "Password123"
    )
    @field:NotBlank
    @field:Size(min = 8, max = 64, message = "Пароль должен быть от 8 до 64 символов")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
        message = "Пароль может содержать только буквы латинского алфавита или цифры"
    )
    val newPassword: String
) {
}