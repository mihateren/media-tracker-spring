package com.example.mediatracker.service

import com.example.mediatracker.api.dto.auth.login.LoginRequest
import com.example.mediatracker.api.dto.auth.login.LoginResponse
import com.example.mediatracker.api.dto.auth.refresh.RefreshRequest
import com.example.mediatracker.api.dto.auth.refresh.RefreshResponse
import com.example.mediatracker.api.dto.auth.registration.RegistrationRequest
import com.example.mediatracker.api.dto.auth.registration.RegistrationResponse
import com.example.mediatracker.auth.jwt.JwtService
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.logging.Logging
import com.example.mediatracker.domain.repository.UserRepository
import com.example.mediatracker.exception.entity.InvalidCredentialsException
import com.example.mediatracker.exception.entity.InvalidTokenException
import com.example.mediatracker.exception.entity.UserAlreadyExistsException
import com.example.mediatracker.exception.entity.UserNotFoundException
import jakarta.security.auth.message.AuthException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
) : Logging {

    @Transactional
    fun registration(request: RegistrationRequest): RegistrationResponse {

        if (userRepository.existByUsername(request.username)) {
            throw UserAlreadyExistsException("Username '${request.username}' is already registered")
        }
        if (userRepository.existByEmail(request.email)) {
            throw UserAlreadyExistsException("Email '${request.email}' is already registered")
        }

        userRepository.save(
            User(
                username = request.username,
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password),
            )
        )
        return RegistrationResponse("successfully registered")
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, user.passwordHash))
            throw InvalidCredentialsException()

        return LoginResponse(
            accessToken = jwtService.generateAccessToken(user.username),
            refreshToken = jwtService.generateRefreshToken(user.username)
        )
    }

    fun refresh(req: RefreshRequest): RefreshResponse {
        val token = req.refreshToken
        if (!jwtService.validateRefreshToken(token)) throw InvalidTokenException()

        val user = userRepository.findByUsername(jwtService.extractUsername(token))
            ?: throw UserNotFoundException()

        return RefreshResponse(
            accessToken = jwtService.generateAccessToken(user.username)
        )
    }

}