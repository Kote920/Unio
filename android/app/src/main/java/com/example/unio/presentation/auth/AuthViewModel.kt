package com.example.unio.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unio.domain.usecase.LoginUseCase
import com.example.unio.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects: SharedFlow<AuthEffect> = _effects.asSharedFlow()

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UpdateEmail -> _state.update { it.copy(email = intent.email, error = null) }
            is AuthIntent.UpdatePassword -> _state.update { it.copy(password = intent.password, error = null) }
            is AuthIntent.UpdateDisplayName -> _state.update { it.copy(displayName = intent.name, error = null) }
            is AuthIntent.Register -> register()
            is AuthIntent.Login -> login()
            is AuthIntent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(
                email = _state.value.email.trim(),
                password = _state.value.password,
                displayName = _state.value.displayName.takeIf { it.isNotBlank() }
            )
            result.fold(
                onSuccess = { user ->
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(AuthEffect.NavigateToHome(user))
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(
                email = _state.value.email.trim(),
                password = _state.value.password
            )
            result.fold(
                onSuccess = { user ->
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(AuthEffect.NavigateToHome(user))
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}
