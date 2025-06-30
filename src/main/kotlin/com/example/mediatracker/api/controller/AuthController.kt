package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.auth.login.LoginRequest
import com.example.mediatracker.api.dto.auth.login.LoginResponse
import com.example.mediatracker.api.dto.auth.refresh.RefreshRequest
import com.example.mediatracker.api.dto.auth.refresh.RefreshResponse
import com.example.mediatracker.api.dto.auth.registration.RegistrationRequest
import com.example.mediatracker.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    fun registration(@Valid @RequestBody request: RegistrationRequest) =
        authService.registration(request)

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse =
        authService.login(request)


    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshRequest): RefreshResponse =
        authService.refresh(request)


}