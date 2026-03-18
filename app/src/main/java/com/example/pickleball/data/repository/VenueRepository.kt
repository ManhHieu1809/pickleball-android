package com.example.pickleball.data.repository

import com.example.pickleball.data.model.Venue
import com.example.pickleball.data.remote.VenueApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VenueRepository @Inject constructor(
    private val venueApi: VenueApiService
) {
    suspend fun getActiveVenues(): Result<List<Venue>> {
        return try {
            val response = venueApi.getActiveVenues()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load venues"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNearbyVenues(lat: Double, lng: Double, radius: Double = 10.0): Result<List<Venue>> {
        return try {
            val response = venueApi.getNearbyVenues(lat, lng, radius)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load nearby venues"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVenueById(venueId: Long): Result<Venue> {
        return try {
            val response = venueApi.getVenueById(venueId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Venue not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
