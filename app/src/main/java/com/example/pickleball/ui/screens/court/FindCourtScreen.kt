package com.example.pickleball.ui.screens.court

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.court.components.CompactCourtCard
import com.example.pickleball.ui.screens.court.components.LargeCourtCard
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*

@Composable
fun FindCourtScreen(
    navController: NavController,
    onCourtClick: (String) -> Unit
) {
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

            item {
                LargeCourtCard(
                    title = "Ace Pickleball Club",
                    rating = "4.9",
                    reviewCount = "210",
                    distance = "1.2 mi away • SoMa District",
                    price = "$20",
                    isIndoor = true,
                    tags = listOf("Pro Shop", "Showers"),
                    imageUrl = "https://picsum.photos/400/300?1",
                    onClick = { onCourtClick("court_ace_01") }
                )
            }

            item {
                LargeCourtCard(
                    title = "Golden Gate Park Courts",
                    rating = "4.5",
                    reviewCount = "86",
                    distance = "2.4 mi away • Golden Gate Park",
                    price = "Free",
                    isIndoor = false,
                    tags = listOf("Public", "Lights"),
                    imageUrl = "https://picsum.photos/400/300?2",
                    primaryButtonText = "Join Waitlist",
                    secondaryButtonText = "View Details",
                    onClick = { onCourtClick("court_golden_02") }
                )
            }

            item {
                CompactCourtCard(
                    title = "Sunset Rec Center",
                    price = "$15",
                    distance = "3.1 mi away • Outdoor",
                    statusText = "3 Courts Open",
                    statusColor = Color(0xFF00C853),
                    imageUrl = "https://picsum.photos/200/200?3",
                    onClick = { onCourtClick("court_sunset_03") }
                )
            }

            item {
                CompactCourtCard(
                    title = "The Post",
                    price = "$45",
                    distance = "5.2 mi away • Indoor",
                    statusText = "Busy",
                    statusColor = Color.Red,
                    imageUrl = "https://picsum.photos/200/200?4",
                    onClick = { onCourtClick("court_post_04") }
                )
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("End of results", color = TextSecondaryLight, fontSize = 12.sp)
                }
            }
        }
    }
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
            FilterChip(text = "San Francisco", isActive = true, icon = Icons.Default.Close)
        }
        item {
            FilterChip(text = "Availability", isActive = false, icon = Icons.Outlined.CalendarMonth)
        }
        item {
            FilterChip(text = "Type", isActive = false, icon = Icons.Outlined.FilterAlt) // Icon minh họa
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