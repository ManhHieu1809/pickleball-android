package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.*
import com.example.pickleball.data.repository.BookingRepository
import com.example.pickleball.data.repository.CourtRepository
import com.example.pickleball.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val courtRepository: CourtRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _slotsState = MutableStateFlow<UiState<List<TimeSlot>>>(UiState.Idle)
    val slotsState: StateFlow<UiState<List<TimeSlot>>> = _slotsState.asStateFlow()

    private val _bookingState = MutableStateFlow<UiState<Booking>>(UiState.Idle)
    val bookingState: StateFlow<UiState<Booking>> = _bookingState.asStateFlow()

    private val _myBookingsState = MutableStateFlow<UiState<List<Booking>>>(UiState.Idle)
    val myBookingsState: StateFlow<UiState<List<Booking>>> = _myBookingsState.asStateFlow()

    private val _courtsState = MutableStateFlow<UiState<List<Court>>>(UiState.Idle)
    val courtsState: StateFlow<UiState<List<Court>>> = _courtsState.asStateFlow()

    private val _courtDetailState = MutableStateFlow<UiState<Court>>(UiState.Idle)
    val courtDetailState: StateFlow<UiState<Court>> = _courtDetailState.asStateFlow()

    private val _casualMatchesState = MutableStateFlow<UiState<List<CasualMatchDTO>>>(UiState.Idle)
    val casualMatchesState: StateFlow<UiState<List<CasualMatchDTO>>> = _casualMatchesState.asStateFlow()

    private val _matchCandidatesState = MutableStateFlow<UiState<List<PlayerMatchDTO>>>(UiState.Idle)
    val matchCandidatesState: StateFlow<UiState<List<PlayerMatchDTO>>> = _matchCandidatesState.asStateFlow()

    private val _casualMatchDetailState = MutableStateFlow<UiState<CasualMatchDTO>>(UiState.Idle)
    val casualMatchDetailState: StateFlow<UiState<CasualMatchDTO>> = _casualMatchDetailState.asStateFlow()


    private val _rankedMatchesState = MutableStateFlow<UiState<List<RankedMatchDTO>>>(UiState.Idle)
    val rankedMatchesState: StateFlow<UiState<List<RankedMatchDTO>>> = _rankedMatchesState.asStateFlow()

    private val _rankedMatchDetailState = MutableStateFlow<UiState<RankedMatchDTO>>(UiState.Idle)
    val rankedMatchDetailState: StateFlow<UiState<RankedMatchDTO>> = _rankedMatchDetailState.asStateFlow()

    private val _checkInState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val checkInState: StateFlow<UiState<Unit>> = _checkInState.asStateFlow()

    private val _submitResultState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitResultState: StateFlow<UiState<Unit>> = _submitResultState.asStateFlow()

    private val _confirmResultState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val confirmResultState: StateFlow<UiState<Unit>> = _confirmResultState.asStateFlow()

    private val _disputeState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val disputeState: StateFlow<UiState<Unit>> = _disputeState.asStateFlow()

    fun loadAvailableSlots(courtId: Long, date: String) {
        viewModelScope.launch {
            _slotsState.value = UiState.Loading
            courtRepository.getAllSlots(courtId, date)
                .onSuccess { _slotsState.value = UiState.Success(it) }
                .onFailure { _slotsState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun loadCourtsByVenue(venueId: Long) {
        viewModelScope.launch {
            _courtsState.value = UiState.Loading
            courtRepository.getCourtsByVenue(venueId)
                .onSuccess { courts -> 
                    _courtsState.value = UiState.Success(courts.filter { it.isActive }) 
                }
                .onFailure { _courtsState.value = UiState.Error(it.message ?: "Failed loading courts for venue") }
        }
    }

    fun loadCourtById(courtId: Long) {
        viewModelScope.launch {
            _courtDetailState.value = UiState.Loading
            courtRepository.getCourtById(courtId)
                .onSuccess { _courtDetailState.value = UiState.Success(it) }
                .onFailure { _courtDetailState.value = UiState.Error(it.message ?: "Failed loading court detail") }
        }
    }

    fun createBooking(courtId: Long, startTime: String, endTime: String, type: String = "PRIVATE") {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.createBooking(CreateBookingRequest(courtId, startTime, endTime, type, null, userId))
                        .onSuccess { _bookingState.value = UiState.Success(it) }
                        .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
                } ?: run {
                    _bookingState.value = UiState.Error("User ID is null")
                }
            }.onFailure {
                _bookingState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun joinBooking(bookingId: Long) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            bookingRepository.joinBooking(bookingId)
                .onSuccess { _bookingState.value = UiState.Success(it) }
                .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun joinCasualMatch(bookingId: Long) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                val userId = user.id
                if (userId != null) {
                    bookingRepository.joinBooking(bookingId, userId)
                        .onSuccess {
                            _bookingState.value = UiState.Success(it)
                            // Refresh candidates after joining
                            loadMatchCandidates(bookingId) 
                        }
                        .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
                } else {
                    _bookingState.value = UiState.Error("User ID is null")
                }
            }.onFailure {
                _bookingState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            bookingRepository.cancelBooking(bookingId)
                .onSuccess { _bookingState.value = UiState.Success(it) }
                .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun loadBookingDetail(bookingId: Long) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            bookingRepository.getBookingById(bookingId)
                .onSuccess { _bookingState.value = UiState.Success(it) }
                .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun loadMyBookings() {
        viewModelScope.launch {
            _myBookingsState.value = UiState.Loading
            
            // fetch user first
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.getMyBookings(userId)
                        .onSuccess { _myBookingsState.value = UiState.Success(it) }
                        .onFailure { _myBookingsState.value = UiState.Error(it.message ?: "Failed loading my bookings") }
                } ?: run {
                    _myBookingsState.value = UiState.Error("User ID is null")
                }
            }.onFailure {
                _myBookingsState.value = UiState.Error(it.message ?: "Failed to get current user")
            }
        }
    }

    fun loadAvailableCasualMatches() {
        viewModelScope.launch {
            _casualMatchesState.value = UiState.Loading
            bookingRepository.getAvailableCasualMatches()
                .onSuccess { _casualMatchesState.value = UiState.Success(it) }
                .onFailure { _casualMatchesState.value = UiState.Error(it.message ?: "Failed loading matches") }
        }
    }

    fun loadMatchCandidates(bookingId: Long) {
        viewModelScope.launch {
            _matchCandidatesState.value = UiState.Loading
            bookingRepository.getCasualMatchCandidates(bookingId)
                .onSuccess { _matchCandidatesState.value = UiState.Success(it) }
                .onFailure { _matchCandidatesState.value = UiState.Error(it.message ?: "Failed loading candidates") }
        }
    }

    fun createCasualMatch(courtId: Long, startTime: String, endTime: String, notes: String?) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.createCasualMatch(
                        CreateBookingRequest(courtId, startTime, endTime, "CASUAL", notes, userId)
                    )
                        .onSuccess { 
                            _casualMatchDetailState.value = UiState.Success(it)
                            _bookingState.value = UiState.Success(it.booking) 
                        } // Map to booking success for UI uniformity
                        .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
                } ?: run {
                    _bookingState.value = UiState.Error("User ID is null")
                }
            }.onFailure {
                _bookingState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun resetBookingState() { _bookingState.value = UiState.Idle }

    fun loadAvailableRankedMatches() {
        viewModelScope.launch {
            _rankedMatchesState.value = UiState.Loading
            bookingRepository.getAvailableRankedMatches()
                .onSuccess { _rankedMatchesState.value = UiState.Success(it) }
                .onFailure { _rankedMatchesState.value = UiState.Error(it.message ?: "Failed loading ranked matches") }
        }
    }

    fun loadRankedMatchCandidates(bookingId: Long) {
        viewModelScope.launch {
            _rankedMatchDetailState.value = UiState.Loading
            bookingRepository.getRankedMatchCandidates(bookingId)
                .onSuccess { _rankedMatchDetailState.value = UiState.Success(it) }
                .onFailure { _rankedMatchDetailState.value = UiState.Error(it.message ?: "Failed loading ranked candidates") }
        }
    }

    fun createRankedMatch(courtId: Long, startTime: String, endTime: String, notes: String?) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.createRankedMatch(
                        CreateBookingRequest(courtId, startTime, endTime, "RANKED", notes, userId)
                    )
                        .onSuccess { 
                            _rankedMatchDetailState.value = UiState.Success(it)
                            _bookingState.value = UiState.Success(it.booking) 
                        }
                        .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
                } ?: run {
                    _bookingState.value = UiState.Error("User ID is null")
                }
            }.onFailure {
                _bookingState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun checkIn(bookingId: Long, lat: Double, lng: Double) {
        viewModelScope.launch {
            _checkInState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.checkIn(bookingId, CheckInRequest(userId, "GPS", lat, lng))
                        .onSuccess { _checkInState.value = UiState.Success(Unit) }
                        .onFailure { _checkInState.value = UiState.Error(it.message ?: "Check-in failed") }
                }
            }.onFailure {
                _checkInState.value = UiState.Error("Failed to authenticate user")
            }
        }
    }

    fun resetCheckInState() { _checkInState.value = UiState.Idle }

    fun submitMatchResult(bookingId: Long, teamA: Int, teamB: Int, winner: String) {
        viewModelScope.launch {
            _submitResultState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.submitMatchResult(bookingId, SubmitMatchResultRequest(userId, teamA, teamB, winner))
                        .onSuccess { _submitResultState.value = UiState.Success(Unit) }
                        .onFailure { _submitResultState.value = UiState.Error(it.message ?: "Submit failed") }
                }
            }.onFailure { _submitResultState.value = UiState.Error("Failed to authenticate user") }
        }
    }

    fun confirmMatchResult(bookingId: Long, isConfirmed: Boolean) {
        viewModelScope.launch {
            _confirmResultState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.confirmMatchResult(bookingId, ConfirmMatchResultRequest(userId, isConfirmed))
                        .onSuccess { _confirmResultState.value = UiState.Success(Unit) }
                        .onFailure { _confirmResultState.value = UiState.Error(it.message ?: "Confirm failed") }
                }
            }.onFailure { _confirmResultState.value = UiState.Error("Failed to authenticate user") }
        }
    }

    fun submitDispute(bookingId: Long, reason: String, evidence: String? = null) {
        viewModelScope.launch {
            _disputeState.value = UiState.Loading
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                user.id?.let { userId ->
                    bookingRepository.submitDispute(bookingId, SubmitDisputeRequest(bookingId, userId, reason, evidence))
                        .onSuccess { _disputeState.value = UiState.Success(Unit) }
                        .onFailure { _disputeState.value = UiState.Error(it.message ?: "Dispute failed") }
                }
            }.onFailure { _disputeState.value = UiState.Error("Failed to authenticate user") }
        }
    }

    fun resetPostMatchStates() {
        _submitResultState.value = UiState.Idle
        _confirmResultState.value = UiState.Idle
        _disputeState.value = UiState.Idle
    }
}
