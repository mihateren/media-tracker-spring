package com.example.mediatracker.api.dto.users

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class UpdateUserRequest(

    @field:Size(min = 6, max = 50, message = "Длина username — от 6 до 50 символов")
    val username: String?,

    @field:Pattern(
        regexp = "^https?://.+",
        message = "avatarUrl должен начинаться с http:// или https://"
    )
    val avatarUrl: String?
) {
}