package com.example.mediatracker.api.dto.user

import com.example.mediatracker.common.constants.maxUsernameLength
import com.example.mediatracker.common.constants.minUsernameLength
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
        min = minUsernameLength,
        max = maxUsernameLength,
        message = "Длина username — от ${minUsernameLength} до " +
                "${maxUsernameLength} символов"
    )
    val username: String?,

    @field:Pattern(
        regexp = "^https?://.+",
        message = "avatarUrl должен начинаться с http:// или https://"
    )
    val avatarUrl: String?
) {
}