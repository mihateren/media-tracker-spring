package com.example.mediatracker.api.dto.auth

import com.example.mediatracker.common.constants.maxPasswordLength
import com.example.mediatracker.common.constants.minPasswordLength
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginRequest(

    @field:Schema(
        description = "Адрес электронной почты",
        example = "pivo@example.com"
    )
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат e-mail")
    val email: String,

    @field:Schema(
        description = "Пароль (мин. ${minPasswordLength} символов, буквы и цифры)",
        example = "Password123"
    )
    @field:NotBlank
    @field:Size(
        min = minPasswordLength,
        max = maxPasswordLength,
        message = "Пароль должен быть от ${minPasswordLength}" +
                " до ${maxPasswordLength} символов"
    )
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
        message = "Пароль может содержать только буквы латинского алфавита или цифры"
    )
    val password: String
) {
}