package com.example.unio.data.remote

import com.example.unio.data.remote.dto.RegisterRequest
import com.example.unio.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @GET("api/v1/auth/me")
    suspend fun me(): UserResponse
}
