package com.example.pickleball.data.remote

import com.example.pickleball.data.model.ApiResponse
import com.example.pickleball.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): Response<ApiResponse<User>>
}
