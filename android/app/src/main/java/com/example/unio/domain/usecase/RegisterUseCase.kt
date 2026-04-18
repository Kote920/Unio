package com.example.unio.domain.usecase

import com.example.unio.domain.model.User
import com.example.unio.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, displayName: String?): Result<User> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email is required"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }
        return authRepository.register(email, password, displayName)
    }
}
