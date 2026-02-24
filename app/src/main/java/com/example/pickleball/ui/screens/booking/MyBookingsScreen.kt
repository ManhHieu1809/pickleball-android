package com.example.pickleball.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*

val SoftMint = Color(0xFFD6FFF3)
val CoolGray = Color(0xFFE8EBF0)

@Composable
fun MyBookingsScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            MyBookingsTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        bottomBar = {
            BottomNav(
                selectedTab = HomeTab.BOOKINGS, // Tab hiện tại là BOOKINGS
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.HOME -> navController.navigateToTab(Routes.HOME)
                        HomeTab.MATCHES -> navController.navigateToTab(Routes.FIND_MATCH)
                        HomeTab.COURTS -> navController.navigateToTab(Routes.FIND_COURT)
                        HomeTab.BOOKINGS -> { /* Đang ở đây */ }
                        HomeTab.PROFILE -> navController.navigateToTab(Routes.PROFILE)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SoftMint.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp) // Bottom padding cho BottomNav nổi
            ) {
                item {
                    BookingCard(
                        status = BookingStatus.CONFIRMED,
                        venueName = "Skyline Pickleball Center",
                        courtName = "Court 4 (Hard Surface)",
                        date = "Nov 14, 2023",
                        time = "14:00 - 15:30",
                        badgeText = "Confirmed",
                        onManageClick = {
                            navController.navigate(com.example.pickleball.navigation.Routes.BOOKING_DETAILS)
                        }
                    )
                }

                item {
                    BookingCard(
                        status = BookingStatus.AWAITING_PAYMENT,
                        venueName = "Metro Sports Complex",
                        courtName = "Court 12",
                        date = "Nov 18, 2023",
                        time = "09:00 - 10:00",
                        badgeText = "Awaiting"
                    )
                }

                item {
                    BookingCard(
                        status = BookingStatus.UPCOMING,
                        venueName = "The Pickle Jar",
                        courtName = "Pro Court 1",
                        date = "Dec 02",
                        time = "18:00",
                        badgeText = "Upcoming"
                    )
                }
            }
        }
    }
}

@Composable
fun MyBookingsTopBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.8f)) // Backdrop blur simulation
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Bookings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDeep
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .background(Color.Transparent, CircleShape)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = NavyDeep)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGray, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            TabButton(
                text = "Upcoming",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Past",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f)
            )
        }
        Divider(color = CoolGray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) Color.White else Color.Transparent
    val textColor = if (isSelected) NavyDeep else NavyDeep.copy(alpha = 0.6f)
    val shadowElevation = if (isSelected) 2.dp else 0.dp

    Surface(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(6.dp),
        color = bgColor,
        shadowElevation = shadowElevation
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

enum class BookingStatus { CONFIRMED, AWAITING_PAYMENT, UPCOMING }

@Composable
fun BookingCard(
    status: BookingStatus,
    venueName: String,
    courtName: String,
    date: String,
    time: String,
    badgeText: String,
    onManageClick: () -> Unit = {},
) {
    val badgeColor = when (status) {
        BookingStatus.CONFIRMED -> PrimaryGreen
        BookingStatus.AWAITING_PAYMENT -> SoftMint
        BookingStatus.UPCOMING -> Color.White
    }
    val badgeBorder = if (status == BookingStatus.UPCOMING) BorderStroke(1.dp, CoolGray) else null
    val icon = when (status) {
        BookingStatus.CONFIRMED -> Icons.Default.CheckCircle
        BookingStatus.AWAITING_PAYMENT -> Icons.Default.HourglassTop
        BookingStatus.UPCOMING -> Icons.Default.Event
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray.copy(alpha = 0.5f)), // Nhạt hơn background
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // shadow-soft
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                if(status == BookingStatus.CONFIRMED) PrimaryGreen.copy(0.1f) else SoftMint.copy(0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = if (venueName.contains("Indoor")) "INDOOR" else "OUTDOOR", // Logic giả định
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep.copy(0.5f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(venueName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                    }

                    Surface(
                        color = badgeColor,
                        border = badgeBorder,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, null, modifier = Modifier.size(14.dp), tint = NavyDeep)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(badgeText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = NavyDeep.copy(0.7f), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(courtName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.7f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(0.6f), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(0.5f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(32.dp).background(if(status == BookingStatus.AWAITING_PAYMENT) Color.White else SoftMint, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CalendarMonth, null, tint = NavyDeep, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("DATE", style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f))
                            Text(date, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }

                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(NavyDeep.copy(0.1f)))
                    Spacer(modifier = Modifier.width(12.dp))

                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(32.dp).background(if(status == BookingStatus.AWAITING_PAYMENT) Color.White else SoftMint, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Schedule, null, tint = NavyDeep, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("TIME", style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f))
                            Text(time, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }
                }

                if (status == BookingStatus.CONFIRMED) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onManageClick,
                            modifier = Modifier.weight(1f).height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, NavyDeep.copy(0.1f))
                        ) {
                            Text("Manage", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Directions", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                } else if (status == BookingStatus.AWAITING_PAYMENT) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoftMint, contentColor = NavyDeep),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, PrimaryGreen)
                    ) {
                        Text("Complete Payment", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

