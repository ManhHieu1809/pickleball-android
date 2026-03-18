package com.example.pickleball.data.repository

import com.example.pickleball.data.model.User
import com.example.pickleball.data.remote.UserApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApiService
) {
    suspend fun getUserById(userId: Long): Result<User> {
        return try {
            val response = userApi.getUserById(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
