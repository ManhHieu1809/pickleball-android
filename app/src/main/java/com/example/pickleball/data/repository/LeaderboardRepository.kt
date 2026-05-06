package com.example.pickleball.data.repository

import com.example.pickleball.data.model.LeaderboardPageDTO
import com.example.pickleball.data.remote.LeaderboardApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApiService
) {
    suspend fun getGlobalLeaderboard(page: Int = 0, size: Int = 20): Result<LeaderboardPageDTO> {
        return try {
            val response = leaderboardApi.getGlobalLeaderboard(page, size)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch leaderboard"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
