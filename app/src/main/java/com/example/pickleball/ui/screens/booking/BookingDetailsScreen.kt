package com.example.pickleball.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pickleball.data.model.Booking
import com.example.pickleball.data.model.UiState
import com.example.pickleball.viewmodel.BookingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.pickleball.ui.theme.*

@Composable
fun BookingDetailsScreen(
    bookingId: String?,
    navController: NavController,
    onBackClick: () -> Unit,
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    val bookingState by bookingViewModel.bookingState.collectAsState()
    val booking = (bookingState as? UiState.Success)?.data

    LaunchedEffect(bookingId) {
        bookingId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadBookingDetail(id)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            BookingDetailsTopBar(onBackClick)
        },
        bottomBar = {
            BookingDetailsBottomBar()
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient (Top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SoftMint.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )

            if (bookingState is UiState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else if (booking != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // 1. Venue Header
                    VenueHeaderSection(booking)

                    // 2. Main Booking Info Card
                    BookingInfoCard(booking)

                    // 3. Weather Widget
                    WeatherWidget()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Booking info not found.", color = NavyDeep)
                }
            }
        }
    }
}

@Composable
fun BookingDetailsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "Booking Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = NavyDeep
        )

        IconButton(onClick = { /* More options */ }) {
            Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = NavyDeep)
        }
    }
}

@Composable
fun VenueHeaderSection(booking: Booking) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = booking.venueName ?: "Unknown Venue",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDeep,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = NavyDeep.copy(0.6f))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Venue Address St.", // We don't have address in DTO yet
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = NavyDeep.copy(0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Favorite Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, CoolGray, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun BookingInfoCard(booking: Booking) {
    var monthStr = "MON"
    var dayStr = "01"
    var timeRangeStr = "00:00 - 00:00"
    var durationStr = "Min"

    try {
        val st = LocalDateTime.parse(booking.startTime)
        val et = LocalDateTime.parse(booking.endTime)
        val dateFormatter = DateTimeFormatter.ofPattern("MMM")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        monthStr = st.format(dateFormatter).uppercase()
        dayStr = st.dayOfMonth.toString()
        timeRangeStr = "${st.format(timeFormatter)} - ${et.format(timeFormatter)}"
        val minDiff = java.time.Duration.between(st, et).toMinutes()
        val dayOfWeek = st.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        durationStr = "$dayOfWeek • $minDiff min session"
    } catch (e: Exception) {}

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray), // Màu xám nền thẻ
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            // Decorative Blur (Top Right)
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        Brush.radialGradient(colors = listOf(PrimaryGreen.copy(0.15f), Color.Transparent))
                    )
            )

            Column(modifier = Modifier.padding(24.dp)) {
                // Date & Time Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Date Box
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(monthStr, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f))
                        Text(dayStr, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = NavyDeep)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(timeRangeStr, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Text(durationStr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.6f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = NavyDeep.copy(0.05f))
                Spacer(modifier = Modifier.height(24.dp))

                // Info Grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Col 1
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        InfoItem(label = "COURT", value = booking.courtName ?: "Court")
                        InfoItem(label = "PAYMENT STATUS") {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(
                                    if (booking.status == "CONFIRMED") PrimaryGreen else Color.Gray, 
                                    CircleShape
                                ))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(booking.status ?: "UNKNOWN", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                            }
                        }
                    }

                    // Col 2
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        InfoItem(label = "BOOKING ID", value = "#PB-${booking.id}")
                        InfoItem(label = "TOTAL PRICE", value = "${booking.totalCost ?: 0}")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f), letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
    }
}

@Composable
fun InfoItem(label: String, content: @Composable () -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f), letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
fun WeatherWidget() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftMint.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .border(1.dp, SoftMint, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.WbSunny, null, tint = NavyDeep.copy(0.7f))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Forecast: Sunny, 72°F", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Text("Perfect conditions for outdoor play.", style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = NavyDeep.copy(0.6f))
        }
    }
}

@Composable
fun BookingDetailsBottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Get Directions Button (Primary)
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Directions, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Get Directions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            // Secondary Buttons Row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Invite Players
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoolGray, contentColor = NavyDeep),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Invite Players", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }

                // Manage
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CoolGray),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = NavyDeep)
                ) {
                    Icon(Icons.Default.EditCalendar, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manage", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
