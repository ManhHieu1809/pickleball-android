package com.example.pickleball.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String? = "Bearer",
    val expiresIn: Long? = null,
    val user: User? = null
)

data class User(
    val id: Long,
    val email: String,
    val fullName: String,
    val phoneNumber: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null,
    val location: String? = null,
    val memberSince: String? = null,
    val isActive: Boolean? = true
)
