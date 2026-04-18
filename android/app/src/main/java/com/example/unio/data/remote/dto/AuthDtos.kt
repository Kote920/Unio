package com.example.unio.data.remote.dto

import com.example.unio.domain.model.User

data class RegisterRequest(
    val displayName: String?
)

data class UserResponse(
    val id: String,
    val email: String,
    val displayName: String?,
    val createdAt: String
) {
    fun toDomain() = User(
        id = id,
        email = email,
        displayName = displayName
    )
}
