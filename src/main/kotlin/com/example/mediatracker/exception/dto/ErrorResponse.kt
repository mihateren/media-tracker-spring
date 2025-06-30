package com.example.mediatracker.exception.dto

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?
) {
    companion object {
        fun error(status: HttpStatus, ex: Throwable) =
            ErrorResponse(status.value(), status.reasonPhrase, ex.message)
    }
}