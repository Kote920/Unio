package com.unio.dto

import java.time.Instant
import java.util.UUID

data class RegisterRequest(
    val displayName: String? = null
)

data class UserResponse(
    val id: UUID,
    val email: String,
    val displayName: String?,
    val createdAt: Instant
)
