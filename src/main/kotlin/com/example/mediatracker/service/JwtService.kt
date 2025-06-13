package com.example.mediatracker.service

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun extractUsername(token: String): String?
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
    fun generateToken(userDetails: UserDetails): String
}