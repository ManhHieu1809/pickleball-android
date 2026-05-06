package com.example.pickleball.data.remote

import com.example.pickleball.data.model.ApiResponse
import com.example.pickleball.data.model.LeaderboardPageDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaderboardApiService {
    
    @GET("leaderboard/global")
    suspend fun getGlobalLeaderboard(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<LeaderboardPageDTO>>
}
