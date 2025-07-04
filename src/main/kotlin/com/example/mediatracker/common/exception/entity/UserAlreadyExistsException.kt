package com.example.mediatracker.common.exception.entity

class UserAlreadyExistsException(
    message: String = "Пользователь с таким логином или username уже существует"
) : RuntimeException(message)