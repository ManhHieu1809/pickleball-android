package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.User
import com.example.pickleball.data.repository.AuthRepository
import com.example.pickleball.util.LocationTracker
import com.example.pickleball.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationTracker: LocationTracker,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val userState: StateFlow<UiState<User>> = _userState.asStateFlow()

    private val _eloHistoryState = MutableStateFlow<UiState<List<com.example.pickleball.data.model.EloHistoryRecord>>>(UiState.Idle)
    val eloHistoryState: StateFlow<UiState<List<com.example.pickleball.data.model.EloHistoryRecord>>> = _eloHistoryState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = UiState.Loading
            authRepository.getCurrentUser()
                .onSuccess { 
                    _userState.value = UiState.Success(it)
                    loadEloHistory(it.id)
                }
                .onFailure { _userState.value = UiState.Error(it.message ?: "Failed to load profile") }
        }
    }

    fun loadEloHistory(userId: Long) {
        viewModelScope.launch {
            _eloHistoryState.value = UiState.Loading
            playerRepository.getEloHistory(userId)
                .onSuccess { _eloHistoryState.value = UiState.Success(it) }
                .onFailure { _eloHistoryState.value = UiState.Error(it.message ?: "Failed to load elo history") }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun triggerLocationUpload() {
        locationTracker.fetchAndUploadLocation()
    }
}
