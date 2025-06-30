package com.example.mediatracker.exception.entity

class UserAlreadyExistsException(
    override val message: String = "User with given credentials already exists",
): RuntimeException() {
}