package com.example.mediatracker.common.auth

import com.example.mediatracker.common.exception.entity.InvalidTokenException
import com.example.mediatracker.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(id: String): AuthUserDetails {
        val parsedId = id.toLongOrNull() ?: throw InvalidTokenException()

        val user = userRepository.findById(parsedId)
            ?: throw UsernameNotFoundException("User with id '$parsedId' not found")

        return AuthUserDetails(
            id = user.id!!,
            password = user.passwordHash!!,
            roles = listOf("ROLE_USER")
        )
    }

}