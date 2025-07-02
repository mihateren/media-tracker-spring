package com.example.mediatracker.service

import com.example.mediatracker.api.dto.users.UserDto
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.logging.Logging
import com.example.mediatracker.domain.mapper.UserMapper
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.stereotype.Service

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


}