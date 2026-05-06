package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.remote.TransactionResponse
import com.example.pickleball.data.remote.WalletResponse
import com.example.pickleball.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _walletBalance = MutableStateFlow<WalletResponse?>(null)
    val walletBalance: StateFlow<WalletResponse?> = _walletBalance

    private val _transactions = MutableStateFlow<List<TransactionResponse>>(emptyList())
    val transactions: StateFlow<List<TransactionResponse>> = _transactions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchWalletData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val balanceResult = repository.getWalletBalance()
            balanceResult.onSuccess { data ->
                _walletBalance.value = data
            }.onFailure { e ->
                _error.value = e.message
            }

            val txResult = repository.getTransactionHistory()
            txResult.onSuccess { data ->
                _transactions.value = data
            }.onFailure { e ->
                if (_error.value == null) _error.value = e.message
            }

            _isLoading.value = false
        }
    }

    fun topUp(amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.topUpWallet(amount)
            result.onSuccess { data ->
                _walletBalance.value = data
                fetchWalletData() // Refresh transactions
            }.onFailure { e ->
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun withdraw(amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.withdrawFromWallet(amount)
            result.onSuccess { data ->
                _walletBalance.value = data
                fetchWalletData() // Refresh transactions
            }.onFailure { e ->
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }
}

