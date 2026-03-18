package com.example.pickleball.data.repository

import com.example.pickleball.data.model.Booking
import com.example.pickleball.data.model.CasualMatchDTO
import com.example.pickleball.data.model.CreateBookingRequest
import com.example.pickleball.data.model.JoinBookingRequest
import com.example.pickleball.data.model.PlayerMatchDTO
import com.example.pickleball.data.remote.BookingApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val bookingApi: BookingApiService
) {
    suspend fun createBooking(request: CreateBookingRequest): Result<Booking> {
        return try {
            val response = bookingApi.createBooking(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create booking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinBooking(bookingId: Long): Result<Booking> {
        return try {
            val response = bookingApi.joinBooking(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to join booking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelBooking(bookingId: Long): Result<Booking> {
        return try {
            val response = bookingApi.cancelBooking(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to cancel booking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingById(bookingId: Long): Result<Booking> {
        return try {
            val response = bookingApi.getBookingById(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyBookings(userId: Long): Result<List<Booking>> {
        return try {
            val response = bookingApi.getMyBookings(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch bookings"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCasualMatch(request: CreateBookingRequest): Result<CasualMatchDTO> {
        return try {
            val response = bookingApi.createCasualMatch(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create casual match"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableCasualMatches(): Result<List<CasualMatchDTO>> {
        return try {
            val response = bookingApi.getAvailableCasualMatches()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch available casual matches"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCasualMatchCandidates(bookingId: Long): Result<List<PlayerMatchDTO>> {
        return try {
            val response = bookingApi.getCasualMatchCandidates(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch candidates"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinCasualMatch(bookingId: Long): Result<CasualMatchDTO> {
        return try {
            val response = bookingApi.joinCasualMatch(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to join casual match"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
