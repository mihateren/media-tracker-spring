package com.example.mediatracker.service

import com.example.mediatracker.api.dto.user.ChangeEmailRequest
import com.example.mediatracker.api.dto.user.ChangePasswordRequest
import com.example.mediatracker.api.dto.user.UpdateUserRequest
import com.example.mediatracker.api.dto.user.UserDto
import com.example.mediatracker.common.exception.entity.InvalidCredentialsException
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.domain.mapper.UserMapper
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : Logging {

    fun getUserMe(userId: Long): UserDto? {
        val userFullInfo = userRepository.findWithProfileById(userId)
            ?: throw UserNotFoundException(userId)
        return mapper.userFullInfoToDto(userFullInfo)
    }

    @Transactional
    fun updateUser(userId: Long, request: UpdateUserRequest) {
        require(request.username != null || request.avatarUrl != null) {
            "Пустой PATCH – нечего обновлять"
        }

        request.username?.let {
            userRepository.updateUsername(userId, it)
        }

        request.avatarUrl?.let { url ->
            val profile = userRepository.findProfileById(userId)
                ?: throw UserNotFoundException(userId)
            profile.avatarUrl = url
            userRepository.saveProfile(profile)
        }
    }

    @Transactional
    fun changePassword(userId: Long, request: ChangePasswordRequest) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        if (!passwordEncoder.matches(request.oldPassword, user.passwordHash)) {
            throw InvalidCredentialsException("Неверный текущий пароль")
        }

        if (passwordEncoder.matches(request.newPassword, user.passwordHash)) {
            throw IllegalArgumentException("Новый пароль совпадает со старым")
        }

        user.passwordHash = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    @Transactional
    fun changeEmail(userId: Long, request: ChangeEmailRequest) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw InvalidCredentialsException("Неверный текущий пароль")
        }

        if (user.email == request.email) {
            throw IllegalArgumentException("Новый email не может совпадать со старым")
        }

        user.email = request.email
        userRepository.save(user)
    }

}