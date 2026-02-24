package com.example.pickleball.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pickleball.ui.screens.home.components.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.theme.BackgroundDark
import com.example.pickleball.ui.theme.BackgroundLight

@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(HomeTab.HOME) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item { HomeHeader("Alex", "https://i.pravatar.cc/150") }
            item {
                RatingCard(
                    rating = "1,850",
                    weeklyChange = "+5.2%"
                ) }
            item { QuickActions(
                onJoinRanked = { navController.navigate(Routes.PRE_MATCH_LOBBY) },
                onBookCourt = { /* ... */ },
                onFindCasual = { navController.navigate(Routes.FIND_MATCH) }
            ) }
            item {
                NearbyCourts(
                    courts = listOf(
                        CourtUiModel("Sunset Park", "0.4 mi", "2 Open", "https://picsum.photos/400/300?1"),
                        CourtUiModel("Elite Club Downtown", "1.2 mi", "Busy", "https://picsum.photos/400/300?2")
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
                    HomeTab.HOME -> { /* Đang ở Home, không làm gì hoặc scroll to top */ }
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
