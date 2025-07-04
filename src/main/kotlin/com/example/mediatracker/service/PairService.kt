package com.example.mediatracker.service

import com.example.mediatracker.api.dto.pair.CreatePairResponse
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.domain.repository.PairRepository
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PairService(
    private val jwtService: JwtService,
    private val pairRepository: PairRepository,
    private val userRepository: UserRepository,
) {

    fun generateInviteToken(userId: Long): CreatePairResponse {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User $userId not found")
        return CreatePairResponse(jwtService.generateInviteToken(user))
    }

}