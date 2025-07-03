package com.example.mediatracker.common.exception.entity

class InvalidCredentialsException(
    override var message: String = "Bad credentials"
) : RuntimeException(message)
