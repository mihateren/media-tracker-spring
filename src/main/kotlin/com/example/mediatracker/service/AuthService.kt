package com.example.mediatracker.service

import com.example.mediatracker.api.dto.auth.LoginRequest
import com.example.mediatracker.api.dto.auth.LoginResponse
import com.example.mediatracker.api.dto.auth.RefreshRequest
import com.example.mediatracker.api.dto.auth.RefreshResponse
import com.example.mediatracker.api.dto.auth.RegistrationRequest
import com.example.mediatracker.api.dto.auth.RegistrationResponse
import com.example.mediatracker.auth.jwt.JwtService
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.logging.Logging
import com.example.mediatracker.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
): Logging {

    fun registration(request: RegistrationRequest): RegistrationResponse {
        userRepository.save(
            User(
                username = request.username,
                email = request.email,
                password = passwordEncoder.encode(request.password) ,
            )
        )
        return RegistrationResponse("successfully registered")
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: error("User '${request.username}' not found")

        log.info { "Requested user '${user.username}': '${request.password}'" }
        check(passwordEncoder.matches(request.password, user.password)) {
            "Bad credentials"
        }

        return LoginResponse(
            accessToken = jwtService.generateAccessToken(user.username),
            refreshToken = jwtService.generateRefreshToken(user.username)
        )
    }

    fun refresh(request: RefreshRequest): RefreshResponse {
        val refreshToken = request.refreshToken
        check(jwtService.isValid(refreshToken)) { "Invalid or expired refresh token" }

        val username = jwtService.extractUsername(refreshToken)
        val user = userRepository.findByUsername(username) ?: error("User not found")

        return RefreshResponse(
            accessToken = jwtService.generateAccessToken(user.username),
        )
    }

}