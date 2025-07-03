package com.example.mediatracker.service

import com.example.mediatracker.api.dto.users.UpdateUserRequest
import com.example.mediatracker.api.dto.users.UserDto
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.exception.entity.InvalidTokenException
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.domain.entity.User
import com.example.mediatracker.domain.entity.UserProfile
import com.example.mediatracker.domain.mapper.UserMapper
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val mapper: UserMapper
) : Logging {

    fun getUserMe(userId: Long): UserDto? {
        val userFullInfo = userRepository.findWithProfileById(userId)
            ?: throw EntityNotFoundException("User $userId not found")
        return mapper.userFullInfoToDto(userFullInfo)
    }

    @Transactional
    fun updateUser(userId: Long, request: UpdateUserRequest) {
        val userInfo = userRepository.findById(userId) ?: throw EntityNotFoundException("User $userId not found")

        require(request.username != null || request.avatarUrl != null) {
            "Пустой PATCH – нечего обновлять"
        }

        request.username?.let {
            userRepository.updateUsername(userId, it)
        }

        request.avatarUrl?.let { url ->
            val profile = userRepository.findProfileById(userId)
                ?: throw EntityNotFoundException("User profile $userId not found")
            profile.avatarUrl = url
            userRepository.saveProfile(profile)
        }
    }

}