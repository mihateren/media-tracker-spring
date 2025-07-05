package com.example.mediatracker.common.exception.entity

class UserNotFoundException(
    val userId: Long? = null,
    customMessage: String? = null
) : RuntimeException(
    customMessage ?: if (userId != null)
        "Пользователь с id $userId не найден"
    else
        "Пользователь не найден"
) {
}
