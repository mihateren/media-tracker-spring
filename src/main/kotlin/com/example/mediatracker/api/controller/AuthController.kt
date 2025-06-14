package com.example.mediatracker.api.controller

import com.example.mediatracker.service.JwtService
import com.example.mediatracker.service.impl.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    data class AuthRequest(val username: String, val password: String)
    data class TokenResponse(val token: String)

    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequest) {
        userService.register(request.username, request.password)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): TokenResponse {
        if (userService.validateUser(request.username, request.password)) {
            val details = userService.getInfoByUsername(request.username)
            return TokenResponse(jwtService.generateToken(details))
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
    }
}