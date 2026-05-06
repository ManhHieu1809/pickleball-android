package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.EloHistoryRecord
import com.example.pickleball.data.model.LeaderboardPageDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.repository.AuthRepository
import com.example.pickleball.data.repository.LeaderboardRepository
import com.example.pickleball.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val playerRepository: PlayerRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _leaderboardState = MutableStateFlow<UiState<LeaderboardPageDTO>>(UiState.Idle)
    val leaderboardState: StateFlow<UiState<LeaderboardPageDTO>> = _leaderboardState.asStateFlow()

    private val _eloHistoryState = MutableStateFlow<UiState<List<EloHistoryRecord>>>(UiState.Idle)
    val eloHistoryState: StateFlow<UiState<List<EloHistoryRecord>>> = _eloHistoryState.asStateFlow()

    fun loadGlobalLeaderboard(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _leaderboardState.value = UiState.Loading
            leaderboardRepository.getGlobalLeaderboard(page, size)
                .onSuccess { _leaderboardState.value = UiState.Success(it) }
                .onFailure { _leaderboardState.value = UiState.Error(it.message ?: "Failed to load leaderboard") }
        }
    }

    fun loadMyEloHistory() {
        viewModelScope.launch {
            _eloHistoryState.value = UiState.Loading
            authRepository.getCurrentUser().onSuccess { user ->
                user.id?.let { userId ->
                    playerRepository.getEloHistory(userId)
                        .onSuccess { _eloHistoryState.value = UiState.Success(it) }
                        .onFailure { _eloHistoryState.value = UiState.Error(it.message ?: "Failed to load history") }
                } ?: run {
                    _eloHistoryState.value = UiState.Error("User ID missing")
                }
            }.onFailure {
                _eloHistoryState.value = UiState.Error("Auth error")
            }
        }
    }
}
