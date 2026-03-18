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
            bookingRepository.joinCasualMatch(bookingId)
                .onSuccess {
                    _bookingState.value = UiState.Success(it.booking)
                    // Refresh candidates after joining
                    loadMatchCandidates(bookingId) 
                }
                .onFailure { _bookingState.value = UiState.Error(it.message ?: "Failed") }
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
                        .onSuccess { _bookingState.value = UiState.Success(it.booking) } // Map to booking success for UI uniformity
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
}
