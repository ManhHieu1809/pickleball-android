package com.example.pickleball.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pickleball.ui.screens.home.components.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.theme.BackgroundLight
import com.example.pickleball.viewmodel.ProfileViewModel
import com.example.pickleball.viewmodel.VenueViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    venueViewModel: VenueViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(HomeTab.HOME) }

    // Load dữ liệu thật từ API
    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUser()
        venueViewModel.loadActiveVenues()
    }

    val userState by profileViewModel.userState.collectAsState()
    val venuesState by venueViewModel.venuesState.collectAsState()

    // Lấy tên và avatar từ API, fallback nếu chưa load xong
    val userName = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.fullName.split(" ").firstOrNull() ?: "User"
        else -> "User"
    }
    val avatarUrl = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.avatarUrl ?: ""
        else -> ""
    }

    // Lấy danh sách venues từ API
    val courts = when (venuesState) {
        is UiState.Success -> {
            val venues = (venuesState as UiState.Success).data
            venues.take(4).map { venue ->
                CourtUiModel(
                    name = venue.name ?: "Unknown",
                    distance = venue.address ?: "Unknown location",
                    status = if (venue.isActive) "Open" else "Closed",
                    imageUrl = venue.imageUrl ?: "https://picsum.photos/400/300?${venue.id}"
                )
            }
        }
        else -> emptyList()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item { HomeHeader(userName, avatarUrl) }
            item {
                RatingCard(
                    rating = "1,850",
                    weeklyChange = "+5.2%"
                ) }
            item { QuickActions(
                onJoinRanked = { navController.navigate(Routes.PRE_MATCH_LOBBY) },
                onBookCourt = { navController.navigate(Routes.FIND_COURT) },
                onFindCasual = { navController.navigate(Routes.FIND_MATCH) }
            ) }
            item {
                NearbyCourts(
                    courts = if (courts.isNotEmpty()) courts else listOf(
                        CourtUiModel("Loading...", "", "", "")
                    )
                )
            }
            item {
                UpNextSection(
                    matches = listOf(
                        MatchUiModel("Oct", "24", "Tournament", "10:00 AM", "Semi-Finals vs. The Titans", "Center Court"),
                        MatchUiModel("Oct", "26", "Casual", "06:00 PM", "Doubles Practice", "Court 4, Downtown")
                    )
                )
            }
            item { SeasonStats(42, 5) }
        }

        BottomNav(
            selectedTab = HomeTab.HOME,
            onTabSelected = { tab ->
                when (tab) {
                    HomeTab.HOME -> { /* Đang ở Home */ }
                    HomeTab.MATCHES -> navController.navigateToTab(Routes.FIND_MATCH)
                    HomeTab.COURTS -> navController.navigateToTab(Routes.FIND_COURT)
                    HomeTab.BOOKINGS -> navController.navigateToTab(Routes.MY_BOOKINGS)
                    HomeTab.PROFILE -> navController.navigateToTab(Routes.PROFILE)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }
}
