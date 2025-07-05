package com.example.mediatracker.api.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class ChangeEmailRequest(
    @field:NotBlank
    @field:Schema(
        description = "Новый email",
        example = "pivo@example.com"
    )    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
) {
}