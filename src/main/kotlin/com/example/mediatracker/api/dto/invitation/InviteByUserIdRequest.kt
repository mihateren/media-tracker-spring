package com.example.mediatracker.api.dto.invitation

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class InviteByUserIdRequest(
    @field:NotNull(message = "userId обязателен")
    @field:Positive(message = "userId должен быть положительным")
    val userId: Long
)