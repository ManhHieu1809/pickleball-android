package com.example.pickleball.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.pickleball.data.repository.AuthRepository
import com.example.pickleball.data.repository.PlayerRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationTracker @Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerRepository: PlayerRepository,
    private val authRepository: AuthRepository
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun fetchAndUploadLocation() {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation && !hasCoarseLocation) {
            // No permissions, cannot track location
            return
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    uploadLocation(it.latitude, it.longitude)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun uploadLocation(lat: Double, lng: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val userResult = authRepository.getCurrentUser()
            userResult.onSuccess { user ->
                val userId = user.id
                if (userId != null) {
                    playerRepository.updateLocation(userId, lat, lng)
                }
            }
        }
    }
}
