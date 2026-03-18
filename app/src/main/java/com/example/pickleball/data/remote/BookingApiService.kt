package com.example.pickleball.data.remote

import com.example.pickleball.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface BookingApiService {

    @POST("bookings")
    suspend fun createBooking(@Body request: CreateBookingRequest): Response<ApiResponse<Booking>>

    @POST("bookings/{bookingId}/join")
    suspend fun joinBooking(@Path("bookingId") bookingId: Long, @Body request: JoinBookingRequest? = null): Response<ApiResponse<Booking>>

    @POST("bookings/{bookingId}/cancel")
    suspend fun cancelBooking(@Path("bookingId") bookingId: Long): Response<ApiResponse<Booking>>

    @GET("bookings/{bookingId}")
    suspend fun getBookingById(@Path("bookingId") bookingId: Long): Response<ApiResponse<Booking>>

    @GET("bookings/my")
    suspend fun getMyBookings(@Query("userId") userId: Long): Response<ApiResponse<List<Booking>>>

    @POST("bookings/casual")
    suspend fun createCasualMatch(@Body request: CreateBookingRequest): Response<ApiResponse<CasualMatchDTO>>

    @GET("bookings/casual/available")
    suspend fun getAvailableCasualMatches(): Response<ApiResponse<List<CasualMatchDTO>>>

    @GET("bookings/{bookingId}/candidates")
    suspend fun getCasualMatchCandidates(@Path("bookingId") bookingId: Long): Response<ApiResponse<List<PlayerMatchDTO>>>

    @POST("bookings/{bookingId}/join")
    suspend fun joinCasualMatch(@Path("bookingId") bookingId: Long): Response<ApiResponse<CasualMatchDTO>>
}
