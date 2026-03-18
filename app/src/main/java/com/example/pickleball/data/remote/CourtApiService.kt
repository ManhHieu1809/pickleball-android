package com.example.pickleball.data.remote

import com.example.pickleball.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface CourtApiService {

    @GET("courts/venue/{venueId}")
    suspend fun getCourtsByVenue(@Path("venueId") venueId: Long): Response<ApiResponse<List<Court>>>

    @GET("courts/{courtId}")
    suspend fun getCourtById(@Path("courtId") courtId: Long): Response<ApiResponse<Court>>

    @GET("courts/venue/{venueId}/active")
    suspend fun getActiveCourtsByVenue(@Path("venueId") venueId: Long): Response<ApiResponse<List<Court>>>

    @GET("courts/active")
    suspend fun getAllActiveCourts(): Response<ApiResponse<List<Court>>>
}

interface TimeSlotApiService {

    @GET("courts/{courtId}/slots")
    suspend fun getAvailableSlots(
        @Path("courtId") courtId: Long,
        @Query("date") date: String // yyyy-MM-dd
    ): Response<ApiResponse<List<TimeSlot>>>

    @GET("courts/{courtId}/slots/all")
    suspend fun getAllSlots(
        @Path("courtId") courtId: Long,
        @Query("date") date: String
    ): Response<ApiResponse<List<TimeSlot>>>
}
