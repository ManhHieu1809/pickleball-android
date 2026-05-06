package com.example.pickleball.data.repository

import com.example.pickleball.data.remote.LocationUpdateRequest
import com.example.pickleball.data.remote.PlayerApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(
    private val playerApi: PlayerApiService
) {
    suspend fun updateLocation(userId: Long, lat: Double, lng: Double): Result<String> {
        return try {
            val response = playerApi.updateLocation(
                LocationUpdateRequest(
                    userId = userId,
                    latitude = lat,
                    longitude = lng
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: "Success")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to update location"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlayerProfile(userId: Long): Result<com.example.pickleball.data.model.PlayerMatchDTO> {
        return try {
            val response = playerApi.getPlayerProfile(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get player profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEloHistory(userId: Long): Result<List<com.example.pickleball.data.model.EloHistoryRecord>> {
        return try {
            val response = playerApi.getEloHistory(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get elo history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
