package com.example.pickleball.data.model

data class Booking(
    val id: Long,
    val courtId: Long? = null,
    val courtName: String? = null,
    val venueId: Long? = null,
    val venueName: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val bookingType: String? = null,
    val status: String? = null,
    val hostId: Long? = null,
    val customerName: String? = null,
    val customerPhone: String? = null,
    val paymentMethod: String? = null,
    val notes: String? = null,
    val createdByStaffId: Long? = null,
    val venueFee: Double? = null,
    val platformFee: Double? = null,
    val totalCost: Double? = null,
    val createdAt: String? = null,
    val payment: PaymentInfo? = null
)

data class PaymentInfo(
    val transactionId: String? = null,
    val status: String? = null,
    val amount: Double? = null,
    val currency: String? = null,
    val message: String? = null
)

data class CreateBookingRequest(
    val courtId: Long,
    val startTime: String,
    val endTime: String,
    val bookingType: String = "PRIVATE",
    val notes: String? = null,
    val creatorUserId: Long
)

data class JoinBookingRequest(
    val userId: Long? = null
)

data class PlayerMatchDTO(
    val userId: Long,
    val fullName: String?,
    val currentElo: Int?,
    val loyaltyTier: String?
)

data class CasualMatchDTO(
    val booking: Booking,
    val payment: PaymentInfo?,
    val depositPerPlayer: Double?,
    val depositCurrency: String?,
    val currentPlayerCount: Int?,
    val requiredPlayerCount: Int?,
    val candidates: List<PlayerMatchDTO>?
)

data class RefereeMatchDTO(
    val userId: Long,
    val fullName: String?,
    val trustScore: Double?,
    val totalMatches: Int?
)

data class RankedMatchDTO(
    val booking: Booking,
    val payment: PaymentInfo?,
    val depositPerPlayer: Double?,
    val depositCurrency: String?,
    val venueFee: Double?,
    val refereeFee: Double?,
    val platformFee: Double?,
    val totalCost: Double?,
    val currentPlayerCount: Int?,
    val requiredPlayerCount: Int?,
    val playerCandidates: List<PlayerMatchDTO>?,
    val refereeAssigned: Boolean?,
    val assignedReferee: RefereeMatchDTO?,
    val refereeCandidates: List<RefereeMatchDTO>?,
    val rankedMatchId: Long?,
    val matchStatus: String?
)

data class CheckInRequest(
    val userId: Long,
    val checkInMethod: String = "GPS",
    val latitude: Double,
    val longitude: Double
)

data class SubmitMatchResultRequest(
    val refereeUserId: Long,
    val teamAScore: Int,
    val teamBScore: Int,
    val winningTeam: String
)

data class ConfirmMatchResultRequest(
    val userId: Long,
    val isConfirmed: Boolean
)

data class SubmitDisputeRequest(
    val rankedMatchId: Long,
    val reportingPlayerId: Long,
    val reason: String,
    val evidence: String? = null
)
