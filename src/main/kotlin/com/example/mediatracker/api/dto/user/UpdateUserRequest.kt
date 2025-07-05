package com.example.mediatracker.api.dto.user

import com.example.mediatracker.common.constants.UserDetailsConstants
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class UpdateUserRequest(

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
    val username: String?,

    @field:Pattern(
        regexp = "^https?://.+",
        message = "avatarUrl должен начинаться с http:// или https://"
    )
    val avatarUrl: String?
) {
}