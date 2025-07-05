package com.example.mediatracker.common.exception.entity

class InvalidCredentialsException(
    override var message: String = "Неверный логин или пароль пользователя"
) : RuntimeException(message)
