package com.example.unio.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val token = runBlocking {
                currentUser.getIdToken(false).await().token
            }
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
