package com.example.unio.domain.usecase

import com.example.unio.domain.model.User
import com.example.unio.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email is required"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password is required"))
        }
        return authRepository.login(email, password)
    }
}
