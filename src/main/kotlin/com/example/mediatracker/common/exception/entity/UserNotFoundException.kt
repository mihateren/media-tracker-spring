package com.example.mediatracker.common.exception.entity

class UserNotFoundException(
    override var message: String = "User not found"
) : RuntimeException(message)
