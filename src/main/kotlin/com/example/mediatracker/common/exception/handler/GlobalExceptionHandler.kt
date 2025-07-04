package com.example.mediatracker.common.exception.handler

import com.example.mediatracker.common.exception.dto.ErrorResponse
import com.example.mediatracker.common.exception.entity.EntityNotFoundException
import com.example.mediatracker.common.exception.entity.InvalidCredentialsException
import com.example.mediatracker.common.exception.entity.InvalidTokenException
import com.example.mediatracker.common.exception.entity.InvitationException
import com.example.mediatracker.common.exception.entity.UserAlreadyExistsException
import com.example.mediatracker.common.exception.entity.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private fun buildErrorMessage(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> =
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
        buildErrorMessage(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUser404(ex: UserNotFoundException) =
        buildErrorMessage(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(
        InvalidCredentialsException::class,
        InvalidTokenException::class
    )
    fun handleUnauthorized(ex: RuntimeException) =
        buildErrorMessage(HttpStatus.UNAUTHORIZED, ex.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult
            .allErrors
            .filterIsInstance<FieldError>()
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }

        return buildErrorMessage(HttpStatus.BAD_REQUEST, details)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(ex: EntityNotFoundException) =
        buildErrorMessage(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException) =
        buildErrorMessage(HttpStatus.BAD_REQUEST, ex.message)

    @ExceptionHandler(InvitationException::class)
    fun handleInvitation(ex: InvitationException) =
        buildErrorMessage(HttpStatus.BAD_REQUEST, ex.message)
}
