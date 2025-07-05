package com.example.mediatracker.api.dto.auth

import com.example.mediatracker.common.constants.UserDetailsConstants
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegistrationRequest(

    @field:Schema(
        description = "Уникальное имя пользователя",
        example = "pivo123"
    )
    @field:NotBlank
    @field:Size(
        min = UserDetailsConstants.minUsernameLength,
        max = UserDetailsConstants.maxUsernameLength,
        message = "Длина username — от ${UserDetailsConstants.minUsernameLength} до " +
                "${UserDetailsConstants.maxUsernameLength} символов"
    )
    val username: String,

    @field:Schema(
        description = "Пароль (мин. ${UserDetailsConstants.minPasswordLength} символов, буквы и цифры)",
        example = "Password123"
    )
    @field:NotBlank
    @field:Size(
        min = UserDetailsConstants.minPasswordLength,
        max = UserDetailsConstants.maxPasswordLength,
        message = "Пароль должен быть от ${UserDetailsConstants.minPasswordLength}" +
                " до ${UserDetailsConstants.maxPasswordLength} символов"
    )
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
        message = "Пароль может содержать только буквы латинского алфавита или цифры"
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