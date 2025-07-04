package com.example.mediatracker.service

import com.example.mediatracker.common.exception.entity.UserNotFoundException
import com.example.mediatracker.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PairService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {


}