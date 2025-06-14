package com.example.mediatracker.service.impl

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class UserService {
    private val users = ConcurrentHashMap<String, String>()

    fun register(username: String, password: String) {
        users[username] = password
    }

    fun validateUser(username: String, password: String): Boolean =
        users[username] == password

    fun getInfoByUsername(username: String): UserDetails {
        val pwd = users[username] ?: throw IllegalArgumentException("User not found")
        return User.withUsername(username).password(pwd).authorities("USER").build()
    }
}