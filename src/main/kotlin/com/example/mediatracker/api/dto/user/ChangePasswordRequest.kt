package com.example.mediatracker.api.dto.user

import com.example.mediatracker.common.constants.UserDetailsConstants
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class ChangePasswordRequest(

    @field:NotBlank
    val oldPassword: String,

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
    val newPassword: String
) {
}