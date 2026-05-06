package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.*
import com.example.pickleball.data.repository.AuthRepository
import com.example.pickleball.data.repository.BookingRepository
import com.example.pickleball.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Lưu vị trí GPS hiện tại của người dùng để dùng khi join queue
var cachedUserLatitude: Double = 21.028511
var cachedUserLongitude: Double = 105.804817

@HiltViewModel
class RankedMatchViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val playerRepository: PlayerRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _playerProfileState = MutableStateFlow<UiState<PlayerMatchDTO>>(UiState.Idle)
    val playerProfileState: StateFlow<UiState<PlayerMatchDTO>> = _playerProfileState.asStateFlow()

    private val _rankedMatchesState = MutableStateFlow<UiState<List<RankedMatchDTO>>>(UiState.Idle)
    val rankedMatchesState: StateFlow<UiState<List<RankedMatchDTO>>> = _rankedMatchesState.asStateFlow()

    private val _matchmakingState = MutableStateFlow<UiState<RankedMatchDTO>>(UiState.Idle)
    val matchmakingState: StateFlow<UiState<RankedMatchDTO>> = _matchmakingState.asStateFlow()

    private val _joinState = MutableStateFlow<UiState<Booking>>(UiState.Idle)
    val joinState: StateFlow<UiState<Booking>> = _joinState.asStateFlow()

    private val _acceptMatchState = MutableStateFlow<UiState<RankedMatchDTO>>(UiState.Idle)
    val acceptMatchState: StateFlow<UiState<RankedMatchDTO>> = _acceptMatchState.asStateFlow()

    private var pollingJob: Job? = null

    init {
        loadPlayerProfile()
    }

    fun loadPlayerProfile() {
        viewModelScope.launch {
            _playerProfileState.value = UiState.Loading
            authRepository.getCurrentUser().onSuccess { user ->
                playerRepository.getPlayerProfile(user.id)
                    .onSuccess {
                        _playerProfileState.value = UiState.Success(it)
                    }
                    .onFailure {
                        _playerProfileState.value = UiState.Error(it.message ?: "Failed to get player profile")
                    }
            }.onFailure {
                _playerProfileState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun startMatchmaking() {
        _matchmakingState.value = UiState.Loading

        viewModelScope.launch {
            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                _matchmakingState.value = UiState.Error("User not logged in")
                return@launch
            }

            // Bước 1: Gọi POST /matchmaking/join để vào hàng chờ
            bookingRepository.joinMatchmakingQueue(
                userId = user.id,
                latitude = cachedUserLatitude,
                longitude = cachedUserLongitude
            ).onSuccess {
                // Bước 2: Poll status cho đến khi backend ghép thành công
                startPollingForMatch(user.id)
            }.onFailure {
                _matchmakingState.value = UiState.Error(it.message ?: "Failed to join queue")
            }
        }
    }

    private fun startPollingForMatch(userId: Long) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(5000) // Poll mỗi 5 giây

                bookingRepository.getMatchmakingStatus(userId)
                    .onSuccess { statusResponse ->
                        if (statusResponse.status == "MATCHED" && statusResponse.match != null) {
                            _matchmakingState.value = UiState.Success(statusResponse.match)
                            pollingJob?.cancel()
                            return@launch
                        }
                        // Nếu status == "WAITING" → tiếp tục poll
                    }
                    .onFailure {
                        // Lỗi mạng thoáng qua: bỏ qua và poll lại lần sau
                    }
            }
        }
    }

    private fun createMockRankedMatch(): RankedMatchDTO {
        return RankedMatchDTO(
            booking = Booking(
                id = 999L,
                courtId = 1L,
                courtName = "Mock Court",
                venueId = 1L,
                venueName = "Mock Venue",
                status = "PENDING",
                bookingType = "RANKED"
            ),
            payment = null,
            depositPerPlayer = 50000.0,
            depositCurrency = "VND",
            venueFee = 100000.0,
            refereeFee = 50000.0,
            platformFee = null,
            totalCost = 150000.0,
            currentPlayerCount = 4,
            requiredPlayerCount = 4,
            playerCandidates = listOf(
                PlayerMatchDTO(1L, "Host", 1300, "SILVER"),
                PlayerMatchDTO(9001L, "Autobot_1", 1250, "SILVER"),
                PlayerMatchDTO(9002L, "Autobot_2", 1260, "SILVER"),
                PlayerMatchDTO(9003L, "Autobot_3", 1270, "SILVER")
            ),
            refereeAssigned = true,
            assignedReferee = null,
            refereeCandidates = listOf(
                RefereeMatchDTO(8888L, "RefBot_Supreme", 9.8, 150)
            ),
            rankedMatchId = 100L,
            matchStatus = "PENDING"
        )
    }

    fun stopMatchmaking() {
        pollingJob?.cancel()
        _matchmakingState.value = UiState.Idle
    }

    fun joinRankedMatch(bookingId: Long) {
        viewModelScope.launch {
            _joinState.value = UiState.Loading
            bookingRepository.joinBooking(bookingId)
                .onSuccess { booking ->
                    _joinState.value = UiState.Success(booking)
                }
                .onFailure {
                    _joinState.value = UiState.Error(it.message ?: "Failed to join")
                }
        }
    }

    /** Bước 7 trong luồng: Accept match + thanh toán cọc */
    fun acceptMatch(bookingId: Long) {
        viewModelScope.launch {
            _acceptMatchState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                _acceptMatchState.value = UiState.Error("User not logged in")
                return@launch
            }
            bookingRepository.acceptMatch(bookingId, user.id)
                .onSuccess { match ->
                    _acceptMatchState.value = UiState.Success(match)
                    // Cập nhật matchmakingState với dữ liệu mới nhất từ server
                    _matchmakingState.value = UiState.Success(match)
                }
                .onFailure {
                    _acceptMatchState.value = UiState.Error(it.message ?: "Failed to accept match")
                }
        }
    }

    fun resetJoinState() {
        _joinState.value = UiState.Idle
    }

    fun resetAcceptMatchState() {
        _acceptMatchState.value = UiState.Idle
    }
}
