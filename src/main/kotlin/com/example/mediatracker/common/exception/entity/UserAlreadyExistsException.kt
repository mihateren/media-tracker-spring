package com.example.mediatracker.common.exception.entity

class UserAlreadyExistsException(
    override val message: String = "User with given credentials already exists",
): RuntimeException() {
}