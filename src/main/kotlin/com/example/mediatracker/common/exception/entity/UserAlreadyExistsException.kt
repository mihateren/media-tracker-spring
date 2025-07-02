package com.example.mediatracker.common.exception.entity

class UserAlreadyExistsException(
    message: String = "User with given credentials already exists"
) : RuntimeException(message)