package com.unio.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    data class ErrorResponse(val message: String)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(e.message ?: "User not found"))
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ErrorResponse(e.message ?: "User already exists"))
    }
}
