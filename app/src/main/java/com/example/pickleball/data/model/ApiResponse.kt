package com.example.pickleball.data.model

data class ApiResponse<T>(
    val success: Boolean = true,
    val message: String? = null,
    val data: T?,
    val timestamp: String? = null
)
