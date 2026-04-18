package com.example.unio.presentation.auth

import com.example.unio.domain.model.User

data class AuthState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface AuthIntent {
    data class UpdateEmail(val email: String) : AuthIntent
    data class UpdatePassword(val password: String) : AuthIntent
    data class UpdateDisplayName(val name: String) : AuthIntent
    data object Register : AuthIntent
    data object Login : AuthIntent
    data object ClearError : AuthIntent
}

sealed interface AuthEffect {
    data class NavigateToHome(val user: User) : AuthEffect
    data class ShowError(val message: String) : AuthEffect
}
