package com.example.mediatracker.common.extension

import com.example.mediatracker.common.exception.entity.InvalidTokenException
import org.springframework.security.oauth2.jwt.Jwt

fun Jwt.userId(): Long =
    subject.toLongOrNull() ?: throw InvalidTokenException()