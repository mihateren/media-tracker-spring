package com.example.mediatracker.domain.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long = 0L,
    val username: String,
    val email: String,
    val password: String,
)
