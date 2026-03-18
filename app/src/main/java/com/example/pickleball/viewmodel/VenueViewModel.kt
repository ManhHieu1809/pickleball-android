package com.example.pickleball.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.Venue
import com.example.pickleball.data.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    private val _venuesState = MutableStateFlow<UiState<List<Venue>>>(UiState.Idle)
    val venuesState: StateFlow<UiState<List<Venue>>> = _venuesState.asStateFlow()

    private val _venueDetailState = MutableStateFlow<UiState<Venue>>(UiState.Idle)
    val venueDetailState: StateFlow<UiState<Venue>> = _venueDetailState.asStateFlow()

    fun loadActiveVenues() {
        viewModelScope.launch {
            _venuesState.value = UiState.Loading
            venueRepository.getActiveVenues()
                .onSuccess { _venuesState.value = UiState.Success(it) }
                .onFailure { _venuesState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun loadNearbyVenues(lat: Double, lng: Double, radius: Double = 10.0) {
        viewModelScope.launch {
            _venuesState.value = UiState.Loading
            venueRepository.getNearbyVenues(lat, lng, radius)
                .onSuccess { _venuesState.value = UiState.Success(it) }
                .onFailure { _venuesState.value = UiState.Error(it.message ?: "Failed") }
        }
    }

    fun loadVenueById(venueId: Long) {
        viewModelScope.launch {
            _venueDetailState.value = UiState.Loading
            venueRepository.getVenueById(venueId)
                .onSuccess { _venueDetailState.value = UiState.Success(it) }
                .onFailure { _venueDetailState.value = UiState.Error(it.message ?: "Failed") }
        }
    }
}
