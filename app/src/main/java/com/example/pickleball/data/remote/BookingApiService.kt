package com.example.pickleball.data.remote

import com.example.pickleball.data.model.*
import retrofit2.Response
import retrofit2.http.*

data class MatchmakingRequest(
    val userId: Long,
    val role: String = "PLAYER",
    val latitude: Double,
    val longitude: Double
)

data class MatchmakingStatusResponse(
    val status: String,        // "WAITING", "MATCHED"
    val bookingId: Long? = null,
    val rankedMatchId: Long? = null,
    val match: RankedMatchDTO? = null
)

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

    @POST("bookings/ranked")
    suspend fun createRankedMatch(@Body request: CreateBookingRequest): Response<ApiResponse<RankedMatchDTO>>

    @GET("bookings/ranked/available")
    suspend fun getAvailableRankedMatches(): Response<ApiResponse<List<RankedMatchDTO>>>

    @GET("bookings/{bookingId}/ranked-candidates")
    suspend fun getRankedMatchCandidates(@Path("bookingId") bookingId: Long): Response<ApiResponse<RankedMatchDTO>>

    @POST("bookings/{bookingId}/check-in")
    suspend fun checkIn(@Path("bookingId") bookingId: Long, @Body request: CheckInRequest): Response<ApiResponse<Void>>

    @POST("bookings/{bookingId}/submit-result")
    suspend fun submitMatchResult(@Path("bookingId") bookingId: Long, @Body request: SubmitMatchResultRequest): Response<ApiResponse<Void>>

    @POST("bookings/{bookingId}/confirm-result")
    suspend fun confirmMatchResult(@Path("bookingId") bookingId: Long, @Body request: ConfirmMatchResultRequest): Response<ApiResponse<Void>>

    @POST("matchmaking/join")
    suspend fun joinMatchmakingQueue(@Body request: MatchmakingRequest): Response<ApiResponse<String>>

    @GET("matchmaking/status")
    suspend fun getMatchmakingStatus(@Query("userId") userId: Long): Response<ApiResponse<MatchmakingStatusResponse>>

    @POST("bookings/{bookingId}/accept-match")
    suspend fun acceptMatch(
        @Path("bookingId") bookingId: Long,
        @Query("userId") userId: Long
    ): Response<ApiResponse<RankedMatchDTO>>

    @POST("bookings/{bookingId}/disputes")
    suspend fun submitDispute(@Path("bookingId") bookingId: Long, @Body request: SubmitDisputeRequest): Response<ApiResponse<Void>>
}
