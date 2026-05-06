package com.example.pickleball.data.remote

import com.example.pickleball.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import com.example.pickleball.data.model.PlayerMatchDTO

data class LocationUpdateRequest(
    val userId: Long,
    val latitude: Double,
    val longitude: Double
)

interface PlayerApiService {
    @PUT("players/location")
    suspend fun updateLocation(@Body request: LocationUpdateRequest): Response<ApiResponse<String>>

    @GET("players/{userId}")
    suspend fun getPlayerProfile(@Path("userId") userId: Long): Response<ApiResponse<PlayerMatchDTO>>

    @GET("players/{userId}/elo-history")
    suspend fun getEloHistory(@Path("userId") userId: Long): Response<ApiResponse<List<com.example.pickleball.data.model.EloHistoryRecord>>>
}
