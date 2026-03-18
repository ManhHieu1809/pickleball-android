package com.example.pickleball.ui.screens.court

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.Venue
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.court.components.CompactCourtCard
import com.example.pickleball.ui.screens.court.components.LargeCourtCard
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.VenueViewModel

@Composable
fun FindCourtScreen(
    navController: NavController,
    onVenueClick: (String) -> Unit,
    venueViewModel: VenueViewModel = hiltViewModel()
) {
    // Load venues từ API
    LaunchedEffect(Unit) {
        venueViewModel.loadActiveVenues()
    }

    val venuesState by venueViewModel.venuesState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = { FindCourtTopBar() },
        bottomBar = {
            BottomNav(
                selectedTab = HomeTab.COURTS,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.HOME -> navController.navigateToTab(Routes.HOME)
                        HomeTab.MATCHES -> navController.navigateToTab(Routes.FIND_MATCH)
                        HomeTab.COURTS -> { /* Đang ở đây */ }
                        HomeTab.BOOKINGS -> navController.navigateToTab(Routes.MY_BOOKINGS)
                        HomeTab.PROFILE -> navController.navigateToTab(Routes.PROFILE)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            item {
                SearchBar()
            }

            item {
                FilterSection()
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nearby Courts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("See Map", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                }
            }

            when (venuesState) {
                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryGreen)
                        }
                    }
                }
                is UiState.Success -> {
                    val venues = (venuesState as UiState.Success<List<Venue>>).data
                    if (venues.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No courts available", color = TextSecondaryLight, fontSize = 14.sp)
                            }
                        }
                    } else {
                        // Hiển thị 2 venue đầu dạng LargeCourtCard
                        val largeVenues = venues.take(2)
                        val compactVenues = venues.drop(2)

                        items(largeVenues) { venue ->
                            LargeCourtCard(
                                title = venue.name ?: "Court",
                                rating = venue.rating?.toString() ?: "N/A",
                                reviewCount = venue.reviewCount?.toString() ?: "0",
                                distance = venue.address ?: "Unknown location",
                                price = formatPrice(venue),
                                isIndoor = false,
                                tags = buildVenueTags(venue),
                                imageUrl = venue.imageUrl ?: "https://picsum.photos/400/300?${venue.id}",
                                onClick = { onVenueClick(venue.id.toString()) }
                            )
                        }

                        items(compactVenues) { venue ->
                            CompactCourtCard(
                                title = venue.name ?: "Court",
                                price = formatPrice(venue),
                                distance = venue.address ?: "Unknown location",
                                statusText = if (venue.isActive) "Open" else "Closed",
                                statusColor = if (venue.isActive) Color(0xFF00C853) else Color.Red,
                                imageUrl = venue.imageUrl ?: "https://picsum.photos/200/200?${venue.id}",
                                onClick = { onVenueClick(venue.id.toString()) }
                            )
                        }
                    }
                }
                is UiState.Error -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Failed to load courts",
                                    color = NavyDeep,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    (venuesState as UiState.Error).message,
                                    color = TextSecondaryLight,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { venueViewModel.loadActiveVenues() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                                ) {
                                    Text("Retry", color = NavyDeep, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                else -> { /* Idle */ }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("End of results", color = TextSecondaryLight, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun formatPrice(venue: Venue): String {
    // Nếu venue có thông tin giá thì hiển thị, không thì hiển thị mặc định
    return venue.description?.let {
        if (it.contains("free", ignoreCase = true)) "Free" else "View"
    } ?: "View"
}

private fun buildVenueTags(venue: Venue): List<String> {
    val tags = mutableListOf<String>()
    if (venue.isActive) tags.add("Active")
    venue.phone?.let { tags.add("Contact") }
    return tags.ifEmpty { listOf("Court") }
}

@Composable
fun FindCourtTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Find a Court", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
        Box {
            Icon(Icons.Outlined.Notifications, null, tint = NavyDeep, modifier = Modifier.size(28.dp))
            Box(modifier = Modifier.size(10.dp).background(PrimaryGreen, CircleShape).align(Alignment.TopEnd).border(1.5.dp, Color.White, CircleShape))
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(SurfaceCardColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, null, tint = TextSecondaryLight)
        Spacer(modifier = Modifier.width(12.dp))
        Text("Search courts or cities...", color = TextSecondaryLight, modifier = Modifier.weight(1f))
        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.Gray.copy(0.3f)))
        Spacer(modifier = Modifier.width(12.dp))
        Icon(Icons.Default.Map, null, tint = NavyDeep.copy(0.6f))
    }
}

@Composable
fun FilterSection() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            FilterChip(text = "All Venues", isActive = true, icon = Icons.Default.Close)
        }
        item {
            FilterChip(text = "Availability", isActive = false, icon = Icons.Outlined.CalendarMonth)
        }
        item {
            FilterChip(text = "Type", isActive = false, icon = Icons.Outlined.FilterAlt)
        }
    }
}

@Composable
fun FilterChip(text: String, isActive: Boolean, icon: ImageVector) {
    Surface(
        color = if (isActive) PrimaryGreen else SurfaceCardColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, fontSize = 13.sp, fontWeight = if(isActive) FontWeight.Bold else FontWeight.Medium, color = NavyDeep)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(icon, null, modifier = Modifier.size(16.dp), tint = NavyDeep)
        }
    }
}