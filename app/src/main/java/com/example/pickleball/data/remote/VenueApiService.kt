package com.example.pickleball.data.remote

import com.example.pickleball.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface VenueApiService {

    @GET("venues/active")
    suspend fun getActiveVenues(): Response<ApiResponse<List<Venue>>>

    @GET("venues/nearby")
    suspend fun getNearbyVenues(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Double = 10.0
    ): Response<ApiResponse<List<Venue>>>

    @GET("venues/{venueId}")
    suspend fun getVenueById(@Path("venueId") venueId: Long): Response<ApiResponse<Venue>>
}
