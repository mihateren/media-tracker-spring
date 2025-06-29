package com.example.mediatracker.api.controller

import com.example.mediatracker.api.dto.auth.LoginRequest
import com.example.mediatracker.api.dto.auth.LoginResponse
import com.example.mediatracker.api.dto.auth.RefreshRequest
import com.example.mediatracker.api.dto.auth.RefreshResponse
import com.example.mediatracker.api.dto.auth.RegistrationRequest
import com.example.mediatracker.api.dto.auth.RegistrationResponse
import com.example.mediatracker.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun registration(
        @RequestBody request: RegistrationRequest
    ): RegistrationResponse = authService.registration(request)

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): LoginResponse = authService.login(request)


    @PostMapping("/refresh")
    fun refresh(
        @RequestBody request: RefreshRequest
    ): RefreshResponse = authService.refresh(request)


}