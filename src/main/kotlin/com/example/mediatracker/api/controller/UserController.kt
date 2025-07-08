package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.user.ChangeEmailRequest
import com.example.mediatracker.api.dto.user.ChangePasswordRequest
import com.example.mediatracker.api.dto.user.UpdateUserRequest
import com.example.mediatracker.api.dto.user.UserDto
import com.example.mediatracker.common.auth.AuthUserDetails
import com.example.mediatracker.common.constants.BEARER_AUTH
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "User")
class UserController(
    private val userService: UserService
) : Logging {

    @GetMapping("/me")
    @Operation(summary = "Получение информации о пользователе")
    fun getUserMe(@AuthenticationPrincipal jwt: AuthUserDetails): UserDto? {
        return userService.getUserMe(jwt.userId())
    }

//    TODO: put запрсоы

    @PutMapping("/me")
    @Operation(summary = "Изменение информации о пользователе")
    fun updateUser(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @Validated @RequestBody user: UpdateUserRequest
    ) {
        userService.updateUser(jwt.userId(), user)
    }

    @PutMapping("/me/password")
    @Operation(summary = "Смена пароля пользователя")
    fun changePassword(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @Validated @RequestBody changePasswordRequest: ChangePasswordRequest
    ) {
        userService.changePassword(jwt.userId(), changePasswordRequest)
    }

    @PutMapping("/me/email")
    @Operation(summary = "Смена email пользователя")
    fun changeEmail(
        @AuthenticationPrincipal jwt: AuthUserDetails,
        @RequestBody changeEmailRequest: ChangeEmailRequest
    ) {
        userService.changeEmail(jwt.userId(), changeEmailRequest)
    }
}

