package com.example.pickleball.data.repository

import com.example.pickleball.data.model.ApiResponse
import com.example.pickleball.data.remote.TransactionResponse
import com.example.pickleball.data.remote.WalletActionRequest
import com.example.pickleball.data.remote.WalletApiService
import com.example.pickleball.data.remote.WalletResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(
    private val apiService: WalletApiService
) {
    suspend fun getWalletBalance(): Result<WalletResponse> {
        return try {
            val response = apiService.getWalletBalance()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get wallet balance"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactionHistory(): Result<List<TransactionResponse>> {
        return try {
            val response = apiService.getTransactionHistory()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get transactions"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun topUpWallet(amount: Double, description: String = "Nạp tiền vào ví"): Result<WalletResponse> {
        return try {
            val response = apiService.topUpWallet(WalletActionRequest(amount, description))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to top up wallet"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun withdrawFromWallet(amount: Double, description: String = "Rút tiền về tài khoản ngân hàng"): Result<WalletResponse> {
        return try {
            val response = apiService.withdrawFromWallet(WalletActionRequest(amount, description))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to withdraw funds"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

