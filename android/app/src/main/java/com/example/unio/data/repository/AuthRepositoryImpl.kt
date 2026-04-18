package com.example.unio.data.repository

import com.example.unio.data.remote.AuthApi
import com.example.unio.data.remote.dto.RegisterRequest
import com.example.unio.domain.model.User
import com.example.unio.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun register(email: String, password: String, displayName: String?): Result<User> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val response = authApi.register(RegisterRequest(displayName))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val response = authApi.me()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
