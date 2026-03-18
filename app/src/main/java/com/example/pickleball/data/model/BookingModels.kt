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
