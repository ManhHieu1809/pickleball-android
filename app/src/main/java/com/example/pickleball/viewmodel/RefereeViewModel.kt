package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.RefereeAssignedMatchDTO
import com.example.pickleball.data.model.RefereeDisputeDTO
import com.example.pickleball.data.model.RefereeProfileDTO
import com.example.pickleball.data.model.RefereeQuestionDTO
import com.example.pickleball.data.model.TestResultResponse
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.CheckInRequest
import com.example.pickleball.data.repository.AuthRepository
import com.example.pickleball.data.repository.BookingRepository
import com.example.pickleball.data.repository.RefereeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefereeViewModel @Inject constructor(
    private val refereeRepository: RefereeRepository,
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _refereeProfileState = MutableStateFlow<UiState<RefereeProfileDTO?>>(UiState.Loading)
    val refereeProfileState: StateFlow<UiState<RefereeProfileDTO?>> = _refereeProfileState

    private val _quizState = MutableStateFlow<UiState<List<RefereeQuestionDTO>>>(UiState.Loading)
    val quizState: StateFlow<UiState<List<RefereeQuestionDTO>>> = _quizState

    private val _submissionState = MutableStateFlow<UiState<TestResultResponse>?>(null)
    val submissionState: StateFlow<UiState<TestResultResponse>?> = _submissionState

    private val _availabilityState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val availabilityState: StateFlow<UiState<Unit>> = _availabilityState

    private val _upcomingMatchesState = MutableStateFlow<UiState<List<RefereeAssignedMatchDTO>>>(UiState.Loading)
    val upcomingMatchesState: StateFlow<UiState<List<RefereeAssignedMatchDTO>>> = _upcomingMatchesState

    private val _historyMatchesState = MutableStateFlow<UiState<List<RefereeAssignedMatchDTO>>>(UiState.Loading)
    val historyMatchesState: StateFlow<UiState<List<RefereeAssignedMatchDTO>>> = _historyMatchesState

    private val _selectedMatch = MutableStateFlow<RefereeAssignedMatchDTO?>(null)
    val selectedMatch: StateFlow<RefereeAssignedMatchDTO?> = _selectedMatch

    private val _matchResultState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val matchResultState: StateFlow<UiState<Unit>> = _matchResultState

    private val _disputesState = MutableStateFlow<UiState<List<RefereeDisputeDTO>>>(UiState.Loading)
    val disputesState: StateFlow<UiState<List<RefereeDisputeDTO>>> = _disputesState

    private val _submitEvidenceState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitEvidenceState: StateFlow<UiState<Unit>> = _submitEvidenceState

    private val _checkInState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val checkInState: StateFlow<UiState<Unit>> = _checkInState

    private val _refereeName = MutableStateFlow<String>("")
    val refereeName: StateFlow<String> = _refereeName

    init {
        checkRefereeStatus()
        viewModelScope.launch {
            authRepository.getCurrentUser().onSuccess { user ->
                _refereeName.value = user.fullName
            }
        }
    }

    private val _isPendingApproval = MutableStateFlow(false)
    val isPendingApproval: StateFlow<Boolean> = _isPendingApproval

    fun checkRefereeStatus() {
        viewModelScope.launch {
            _refereeProfileState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user != null) {
                val result = refereeRepository.getRefereeProfile(user.id)
                result.fold(
                    onSuccess = { profile ->
                        _isPendingApproval.value = false
                        _refereeProfileState.value = UiState.Success(profile)
                    },
                    onFailure = {
                        // Not a referee, check if they have a PENDING request
                        val pendingResult = refereeRepository.hasPendingRequest(user.id)
                        pendingResult.fold(
                            onSuccess = { isPending ->
                                _isPendingApproval.value = isPending
                                _refereeProfileState.value = UiState.Success(null)
                            },
                            onFailure = {
                                _isPendingApproval.value = false
                                _refereeProfileState.value = UiState.Success(null)
                            }
                        )
                    }
                )
            } else {
                _refereeProfileState.value = UiState.Error("User not logged in")
            }
        }
    }

    fun generateQuiz() {
        viewModelScope.launch {
            _quizState.value = UiState.Loading
            val result = refereeRepository.generateTest()
            result.fold(
                onSuccess = { questions ->
                    _quizState.value = UiState.Success(questions)
                },
                onFailure = { error ->
                    _quizState.value = UiState.Error(error.message ?: "Failed to load quiz")
                }
            )
        }
    }

    fun submitQuiz(answers: Map<Long, String>) {
        viewModelScope.launch {
            _submissionState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user != null) {
                val stringAnswers = answers.mapKeys { it.key.toString() }
                val result = refereeRepository.submitTest(user.id, stringAnswers)
                result.fold(
                    onSuccess = { response ->
                        _submissionState.value = UiState.Success(response)
                        if (response.passed) {
                            checkRefereeStatus() // Reload profile state
                        }
                    },
                    onFailure = { error ->
                        _submissionState.value = UiState.Error(error.message ?: "Submission failed")
                    }
                )
            } else {
                _submissionState.value = UiState.Error("User not logged in")
            }
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = null
    }

    fun updateAvailability(isReady: Boolean) {
        viewModelScope.launch {
            _availabilityState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                _availabilityState.value = UiState.Error("User not logged in")
                return@launch
            }

            refereeRepository.updateAvailability(user.id, isReady).fold(
                onSuccess = { _availabilityState.value = UiState.Success(Unit) },
                onFailure = { _availabilityState.value = UiState.Error(it.message ?: "Failed to update availability") }
            )
        }
    }

    fun loadUpcomingMatches() {
        loadMatchesByStatus("UPCOMING", isUpcoming = true)
    }

    fun loadHistoryMatches() {
        loadMatchesByStatus("HISTORY", isUpcoming = false)
    }

    private fun loadMatchesByStatus(status: String, isUpcoming: Boolean) {
        viewModelScope.launch {
            if (isUpcoming) {
                _upcomingMatchesState.value = UiState.Loading
            } else {
                _historyMatchesState.value = UiState.Loading
            }

            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                val errorState = UiState.Error("User not logged in")
                if (isUpcoming) {
                    _upcomingMatchesState.value = errorState
                } else {
                    _historyMatchesState.value = errorState
                }
                return@launch
            }

            refereeRepository.getAssignedMatches(user.id, status).fold(
                onSuccess = { matches ->
                    if (isUpcoming) {
                        _upcomingMatchesState.value = UiState.Success(matches)
                    } else {
                        _historyMatchesState.value = UiState.Success(matches)
                    }
                },
                onFailure = {
                    val errorState = UiState.Error(it.message ?: "Failed to load matches")
                    if (isUpcoming) {
                        _upcomingMatchesState.value = errorState
                    } else {
                        _historyMatchesState.value = errorState
                    }
                }
            )
        }
    }

    fun selectMatch(match: RefereeAssignedMatchDTO) {
        _selectedMatch.value = match
    }

    fun submitSelectedMatchResult(teamAScore: Int, teamBScore: Int, winningTeam: String) {
        viewModelScope.launch {
            _matchResultState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            val match = _selectedMatch.value

            if (user == null || match == null) {
                _matchResultState.value = UiState.Error("Missing user or match context")
                return@launch
            }

            refereeRepository.submitMatchResult(
                matchId = match.rankedMatchId,
                refereeUserId = user.id,
                teamAScore = teamAScore,
                teamBScore = teamBScore,
                winningTeam = winningTeam
            ).fold(
                onSuccess = {
                    _matchResultState.value = UiState.Success(Unit)
                    loadUpcomingMatches()
                    loadHistoryMatches()
                },
                onFailure = {
                    _matchResultState.value = UiState.Error(it.message ?: "Failed to submit result")
                }
            )
        }
    }

    fun loadDisputes() {
        viewModelScope.launch {
            _disputesState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                _disputesState.value = UiState.Error("User not logged in")
                return@launch
            }

            refereeRepository.getDisputes(user.id).fold(
                onSuccess = { _disputesState.value = UiState.Success(it) },
                onFailure = { _disputesState.value = UiState.Error(it.message ?: "Failed to load disputes") }
            )
        }
    }

    fun submitEvidence(disputeId: Long, evidenceUrl: String, responseText: String) {
        viewModelScope.launch {
            _submitEvidenceState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            if (user == null) {
                _submitEvidenceState.value = UiState.Error("User not logged in")
                return@launch
            }

            refereeRepository.submitDisputeEvidence(
                disputeId = disputeId,
                refereeUserId = user.id,
                evidenceUrl = evidenceUrl,
                responseText = responseText
            ).fold(
                onSuccess = {
                    _submitEvidenceState.value = UiState.Success(Unit)
                    loadDisputes()
                },
                onFailure = {
                    _submitEvidenceState.value = UiState.Error(it.message ?: "Failed to submit evidence")
                }
            )
        }
    }

    fun resetMatchResultState() {
        _matchResultState.value = UiState.Idle
    }

    fun resetSubmitEvidenceState() {
        _submitEvidenceState.value = UiState.Idle
    }

    fun submitCheckIn(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _checkInState.value = UiState.Loading
            val user = authRepository.getCurrentUser().getOrNull()
            val match = _selectedMatch.value
            val bookingId = match?.booking?.id

            if (user == null || bookingId == null) {
                _checkInState.value = UiState.Error("Missing user or booking context")
                return@launch
            }

            val request = CheckInRequest(
                userId = user.id,
                checkInMethod = "GPS",
                latitude = latitude,
                longitude = longitude
            )
            
            bookingRepository.checkIn(bookingId, request).fold(
                onSuccess = {
                    _checkInState.value = UiState.Success(Unit)
                },
                onFailure = {
                    _checkInState.value = UiState.Error(it.message ?: "Check-in failed")
                }
            )
        }
    }
    
    fun resetCheckInState() {
        _checkInState.value = UiState.Idle
    }
}
