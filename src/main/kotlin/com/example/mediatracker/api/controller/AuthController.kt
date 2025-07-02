package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.auth.LoginRequest
import com.example.mediatracker.api.dto.auth.LoginResponse
import com.example.mediatracker.api.dto.auth.RefreshRequest
import com.example.mediatracker.api.dto.auth.RefreshResponse
import com.example.mediatracker.api.dto.auth.RegistrationRequest
import com.example.mediatracker.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Регистрация пользователя")
    fun registration(@Valid @RequestBody request: RegistrationRequest) =
        authService.registration(request)

    @PostMapping("/login")
    @Operation(summary = "Логин пользователя")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse =
        authService.login(request)


    @PostMapping("/refresh")
    @Operation(summary = "Рефреш токена")
    fun refresh(@RequestBody request: RefreshRequest): RefreshResponse =
        authService.refresh(request)


}