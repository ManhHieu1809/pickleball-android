package com.example.pickleball.data.repository

import com.example.pickleball.data.model.Court
import com.example.pickleball.data.model.TimeSlot
import com.example.pickleball.data.remote.CourtApiService
import com.example.pickleball.data.remote.TimeSlotApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourtRepository @Inject constructor(
    private val courtApi: CourtApiService,
    private val timeSlotApi: TimeSlotApiService
) {
    suspend fun getCourtsByVenue(venueId: Long): Result<List<Court>> {
        return try {
            val response = courtApi.getCourtsByVenue(venueId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load courts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourtById(courtId: Long): Result<Court> {
        return try {
            val response = courtApi.getCourtById(courtId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Court not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveCourtsByVenue(venueId: Long): Result<List<Court>> {
        return try {
            val response = courtApi.getActiveCourtsByVenue(venueId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllActiveCourts(): Result<List<Court>> {
        return try {
            val response = courtApi.getAllActiveCourts()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableSlots(courtId: Long, date: String): Result<List<TimeSlot>> {
        return try {
            val response = timeSlotApi.getAvailableSlots(courtId, date)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load slots"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllSlots(courtId: Long, date: String): Result<List<TimeSlot>> {
        return try {
            val response = timeSlotApi.getAllSlots(courtId, date)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load slots"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
