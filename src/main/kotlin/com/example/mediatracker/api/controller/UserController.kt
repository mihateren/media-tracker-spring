package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.user.ChangeEmailRequest
import com.example.mediatracker.api.dto.user.ChangePasswordRequest
import com.example.mediatracker.api.dto.user.UpdateUserRequest
import com.example.mediatracker.api.dto.user.UserDto
import com.example.mediatracker.common.constants.SecurityConstants
import com.example.mediatracker.common.extension.userId
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = SecurityConstants.BEARER_AUTH)
@Tag(name = "User")
class UserController(
    private val userService: UserService
) : Logging {

    @GetMapping("/me")
    @Operation(summary = "Получение информации о пользователе")
    fun getUserMe(@AuthenticationPrincipal jwt: Jwt): UserDto? {
        return userService.getUserMe(jwt.userId())
    }

    @PatchMapping("/me")
    @Operation(summary = "Изменение информации о пользователе")
    fun updateUser(
        @AuthenticationPrincipal jwt: Jwt,
        @Validated @RequestBody user: UpdateUserRequest
    ) {
        userService.updateUser(jwt.userId(), user)
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Смена пароля пользователя")
    fun changePassword(
        @AuthenticationPrincipal jwt: Jwt,
        @Validated @RequestBody changePasswordRequest: ChangePasswordRequest
    ) {
        userService.changePassword(jwt.userId(), changePasswordRequest)
    }

    @PatchMapping("/me/email")
    @Operation(summary = "Смена email пользователя")
    fun changeEmail(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody changeEmailRequest: ChangeEmailRequest
    ) {
        userService.changeEmail(jwt.userId(), changeEmailRequest)
    }
}

