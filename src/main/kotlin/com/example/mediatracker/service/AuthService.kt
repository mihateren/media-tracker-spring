package com.example.mediatracker.service

import com.example.jooq.generated.tables.pojos.Users
import com.example.jooq.generated.tables.pojos.UsersProfiles
import com.example.mediatracker.api.dto.auth.*
import com.example.mediatracker.common.auth.AuthUserDetailsService
import com.example.mediatracker.common.exception.entity.*
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val authUserDetailsService: AuthUserDetailsService,
) : Logging {

    @Transactional
    fun registration(req: RegistrationRequest) {
        if (userRepository.existsByUsername(req.username))
            throw UserAlreadyExistsException("Username '${req.username}' уже зарегистрирован")

        if (userRepository.existsByEmail(req.email))
            throw UserAlreadyExistsException("Email '${req.email}' уже зарегистрирован")

        val user = userRepository.save(
            Users(
                username = req.username,
                email = req.email,
                passwordHash = passwordEncoder.encode(req.password)
            )
        )

        userRepository.saveProfile(
            UsersProfiles(userId = user.id!!)
        )
    }

    fun login(req: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(req.email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(req.password, user.passwordHash))
            throw InvalidCredentialsException()

        return LoginResponse(
            accessToken = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user)
        )
    }

    fun refresh(req: RefreshRequest): RefreshResponse {
        val token = req.refreshToken
        val subject = runCatching { jwtService.getSubject(token) }
            .getOrElse { throw InvalidTokenException() }

        val user = userRepository.findByUsername(subject)
            ?: throw UserNotFoundException()

        val userDetails = authUserDetailsService.loadUserByUsername(user.id.toString())

        if (!jwtService.validateRefreshToken(token, userDetails))
            throw InvalidTokenException()

        return RefreshResponse(
            accessToken = jwtService.generateAccessToken(user)
        )
    }

}
