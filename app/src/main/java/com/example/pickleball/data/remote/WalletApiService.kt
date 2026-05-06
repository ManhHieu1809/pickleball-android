package com.example.pickleball.data.remote

import com.example.pickleball.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class WalletResponse(
    val userId: Long,
    val balance: Double,
    val updatedAt: String
)

data class TransactionResponse(
    val transactionId: String,
    val transactionType: String, // "TOP_UP", "WITHDRAW", "PAYMENT", "REFUND"
    val amount: Double,
    val description: String,
    val date: String
)

data class WalletActionRequest(
    val amount: Double,
    val description: String
)

interface WalletApiService {
    @GET("wallet")
    suspend fun getWalletBalance(): Response<ApiResponse<WalletResponse>>

    @GET("wallet/transactions")
    suspend fun getTransactionHistory(): Response<ApiResponse<List<TransactionResponse>>>

    @POST("wallet/topup")
    suspend fun topUpWallet(@Body request: WalletActionRequest): Response<ApiResponse<WalletResponse>>

    @POST("wallet/withdraw")
    suspend fun withdrawFromWallet(@Body request: WalletActionRequest): Response<ApiResponse<WalletResponse>>
}

