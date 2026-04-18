package com.example.unio.domain.repository

import com.example.unio.domain.model.User

interface AuthRepository {
    suspend fun register(email: String, password: String, displayName: String?): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    fun isLoggedIn(): Boolean
}
