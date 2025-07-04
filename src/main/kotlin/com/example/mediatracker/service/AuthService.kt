package com.example.mediatracker.service

import com.example.mediatracker.api.dto.auth.LoginRequest
import com.example.mediatracker.api.dto.auth.LoginResponse
import com.example.mediatracker.api.dto.auth.RefreshRequest
import com.example.mediatracker.api.dto.auth.RefreshResponse
import com.example.mediatracker.api.dto.auth.RegistrationRequest
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.domain.repository.UserRepository
import com.example.mediatracker.common.exception.entity.InvalidCredentialsException
import com.example.mediatracker.common.exception.entity.InvalidTokenException
import com.example.mediatracker.common.exception.entity.UserAlreadyExistsException
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.domain.entity.UserProfile
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
    fun registration(request: RegistrationRequest) {

        if (userRepository.existsByUsername(request.username)) {
            throw UserAlreadyExistsException("Username '${request.username}' уже зарегистрирован")
        }
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("Email '${request.email}' уже зарегистрирован")
        }

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                passwordHash = passwordEncoder.encode(request.password),
            )
        )
        userRepository.saveProfile(
            UserProfile(userId = user.id!!)
        )
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, user.passwordHash))
            throw InvalidCredentialsException()

        return LoginResponse(
            accessToken = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user)
        )
    }

    fun refresh(req: RefreshRequest): RefreshResponse {
        val token = req.refreshToken
        if (!jwtService.validateRefreshToken(token)) throw InvalidTokenException()

        val user = userRepository.findByUsername(jwtService.extractUsername(token))
            ?: throw UserNotFoundException()

        return RefreshResponse(
            accessToken = jwtService.generateAccessToken(user)
        )
    }

}