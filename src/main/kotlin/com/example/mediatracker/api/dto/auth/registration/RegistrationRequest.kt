package com.example.mediatracker.api.dto.auth.registration

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

data class RegistrationRequest(

    @field:Schema(
        description = "Уникальное имя пользователя",
        example = "pivo"
    )
    @field:NotBlank
    @field:Size(min = 6, max = 32, message = "Длина username — от 6 до 32 символов")
    val username: String,

    @field:Schema(
        description = "Пароль (мин. 8 символов, буквы и цифры)",
        example = "Passw0rd!"
    )
    @field:NotBlank
    @field:Size(min = 8, max = 64, message = "Пароль должен быть от 8 до 64 символов")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
        message = "Пароль должен содержать хотя бы одну букву и одну цифру"
    )
    val password: String,

    @field:Schema(
        description = "Адрес электронной почты",
        example = "pivo@example.com"
    )
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат e-mail")
    val email: String

) {
}