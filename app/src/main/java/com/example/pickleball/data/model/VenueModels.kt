package com.example.pickleball.data.model

import com.squareup.moshi.Json

data class Venue(
    val id: Long? = null,
    val name: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isActive: Boolean = true,
    val ownerId: Long? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val phone: String? = null,
    val rating: Double? = null,
    val reviewCount: Int? = null
)

data class Court(
    val id: Long? = null,
    val venueId: Long? = null,
    val courtName: String? = null,
    val courtType: String? = null,
    val isActive: Boolean = true,
    val pricePerHour: Double? = null,
    val priceAmount: Double? = null,
    val description: String? = null,
    val venueName: String? = null
)

data class TimeSlot(
    val id: Long? = null,
    val courtId: Long? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    @Json(name = "booked") val isBooked: Boolean = false,
    @Json(name = "available") val isAvailable: Boolean = true,
    val priceAmount: Double? = null
)
