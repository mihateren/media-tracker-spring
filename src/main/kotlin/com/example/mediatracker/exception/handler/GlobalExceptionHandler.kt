package com.example.mediatracker.exception.handler

import com.example.mediatracker.exception.dto.ErrorResponse
import com.example.mediatracker.exception.entity.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {


    private fun build(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                message = message
            ),
            status
        )

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserExists(ex: UserAlreadyExistsException) =
        build(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUser404(ex: UserNotFoundException) =
        build(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(
        InvalidCredentialsException::class,
        InvalidTokenException::class
    )
    fun handleUnauthorized(ex: RuntimeException) =
        build(HttpStatus.UNAUTHORIZED, ex.message)
}
