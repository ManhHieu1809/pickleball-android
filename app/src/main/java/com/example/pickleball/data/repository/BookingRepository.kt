package com.example.pickleball.data.repository

import com.example.pickleball.data.model.*
import com.example.pickleball.data.remote.BookingApiService
import com.example.pickleball.data.remote.MatchmakingRequest
import com.example.pickleball.data.remote.MatchmakingStatusResponse
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

    suspend fun joinBooking(bookingId: Long, userId: Long? = null): Result<Booking> {
        return try {
            val request = userId?.let { JoinBookingRequest(it) }
            val response = bookingApi.joinBooking(bookingId, request)
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
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch booking"))
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
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch matches"))
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
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch my bookings"))
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

    suspend fun createRankedMatch(request: CreateBookingRequest): Result<RankedMatchDTO> {
        return try {
            val response = bookingApi.createRankedMatch(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create ranked match"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableRankedMatches(): Result<List<RankedMatchDTO>> {
        return try {
            val response = bookingApi.getAvailableRankedMatches()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch available ranked matches"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRankedMatchCandidates(bookingId: Long): Result<RankedMatchDTO> {
        return try {
            val response = bookingApi.getRankedMatchCandidates(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch ranked candidates"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkIn(bookingId: Long, request: CheckInRequest): Result<Unit> {
        return try {
            val response = bookingApi.checkIn(bookingId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Check-in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitMatchResult(bookingId: Long, request: SubmitMatchResultRequest): Result<Unit> {
        return try {
            val response = bookingApi.submitMatchResult(bookingId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to submit result"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmMatchResult(bookingId: Long, request: ConfirmMatchResultRequest): Result<Unit> {
        return try {
            val response = bookingApi.confirmMatchResult(bookingId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to confirm result"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitDispute(bookingId: Long, request: SubmitDisputeRequest): Result<Unit> {
        return try {
            val response = bookingApi.submitDispute(bookingId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to submit dispute"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun joinMatchmakingQueue(userId: Long, latitude: Double, longitude: Double): Result<Unit> {
        return try {
            val request = MatchmakingRequest(userId = userId, role = "PLAYER", latitude = latitude, longitude = longitude)
            val response = bookingApi.joinMatchmakingQueue(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to join matchmaking queue"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMatchmakingStatus(userId: Long): Result<MatchmakingStatusResponse> {
        return try {
            val response = bookingApi.getMatchmakingStatus(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get matchmaking status"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptMatch(bookingId: Long, userId: Long): Result<RankedMatchDTO> {
        return try {
            val response = bookingApi.acceptMatch(bookingId, userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to accept match"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
