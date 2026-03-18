package com.example.pickleball.ui.screens.court

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pickleball.data.model.Court
import com.example.pickleball.data.model.UiState
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.BookingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CourtDetailScreen(
    courtId: String?,
    onBackClick: () -> Unit,
    onCalendarClick: () -> Unit,
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Load court thật từ API
    LaunchedEffect(courtId) {
        courtId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadCourtById(id)
            bookingViewModel.loadAvailableSlots(id, selectedDate.format(DateTimeFormatter.ISO_DATE))
        }
    }

    val courtState by bookingViewModel.courtDetailState.collectAsState()
    val slotsState by bookingViewModel.slotsState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        when (courtState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is UiState.Success -> {
                val court = (courtState as UiState.Success<Court>).data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 90.dp)
                ) {
                    // Cần spacer to top nav
                    Spacer(modifier = Modifier.height(90.dp))
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(court.courtName ?: "Court", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(court.description ?: "Professional Pickleball Court", style = MaterialTheme.typography.bodyMedium, color = TextSecondaryLight)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Select Time Logic
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Select Time", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onCalendarClick() }) {
                                Text("Calendar", color = PrimaryGreenDark, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.Outlined.CalendarMonth, null, tint = PrimaryGreenDark, modifier = Modifier.size(20.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val days = (0..6).map { LocalDate.now().plusDays(it.toLong()) }

                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            days.forEach { dateObj ->
                                DateItem(
                                    day = dateObj.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                                    date = dateObj.dayOfMonth.toString(),
                                    isSelected = dateObj == selectedDate,
                                    onClick = {
                                        selectedDate = dateObj
                                        courtId?.toLongOrNull()?.let {
                                            bookingViewModel.loadAvailableSlots(it, dateObj.format(DateTimeFormatter.ISO_DATE))
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        // Render Slots (tương lai sẽ render list loop qua slotsState)
                        when (slotsState) {
                            is UiState.Loading -> CircularProgressIndicator(color = PrimaryGreen)
                            is UiState.Success -> {
                                val slots = (slotsState as UiState.Success).data
                                if (slots.isEmpty()) {
                                    Text("No available slots for this date.", color = TextSecondaryLight)
                                } else {
                                    slots.forEach { slot ->
                                        SlotItem(
                                            time = "${slot.startTime ?: "N/A"} - ${slot.endTime ?: "N/A"}",
                                            price = "\$${slot.priceAmount ?: court.pricePerHour ?: 20}",
                                            subtext = if (slot.isBooked != true) "Available" else "Booked",
                                            isSelected = false,
                                            isAvailable = slot.isBooked != true
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                            is UiState.Error -> Text("Failed to load slots", color = Color.Red)
                            else -> {}
                        }
                    }
                }

                TopNavOverlay(onBackClick = onBackClick, title = "Court Booking")
                BottomBookingBar(courtPrice = "\$${court.pricePerHour ?: 20}", modifier = Modifier.align(Alignment.BottomCenter))
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load court", fontWeight = FontWeight.Bold, color = NavyDeep)
                    Button(
                        onClick = {
                            courtId?.toLongOrNull()?.let { bookingViewModel.loadCourtById(it) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Retry", color = NavyDeep, fontWeight = FontWeight.Bold)
                    }
                }
                TopNavOverlay(onBackClick = onBackClick, title = "")
            }
            else -> {}
        }
    }
}

@Composable
private fun TopNavOverlay(onBackClick: () -> Unit, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(0.95f))
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.background(SurfaceCardColor, CircleShape).border(1.dp, Color.LightGray.copy(0.4f), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = NavyDeep)
        }
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
        IconButton(
            onClick = {},
            modifier = Modifier.background(SurfaceCardColor, CircleShape).border(1.dp, Color.LightGray.copy(0.4f), CircleShape)
        ) {
            Icon(Icons.Filled.Favorite, null, tint = NavyDeep)
        }
    }
}

@Composable
private fun BottomBookingBar(courtPrice: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("TOTAL PRICE", fontSize = 10.sp, color = TextSecondaryLight, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(courtPrice, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                    Text("/ 1 hour", fontSize = 12.sp, color = TextSecondaryLight, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Book Slot", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
        }
    }
}

@Composable
private fun DateItem(day: String, date: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PrimaryGreen else SurfaceCardColor
    val textColor = if (isSelected) NavyDeep else TextSecondaryLight

    Column(
        modifier = Modifier
            .size(width = 72.dp, height = 84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 0.dp, if (isSelected) PrimaryGreenDark else Color.Transparent, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(day.uppercase(), fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(date, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
    }
}

@Composable
private fun SlotItem(time: String, price: String, subtext: String, isSelected: Boolean, isAvailable: Boolean) {
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val bgColor = if (isSelected) Color.White else SurfaceCardColor
    val shadowElevation = if (isSelected) 4.dp else 0.dp

    Surface(
        modifier = Modifier.fillMaxWidth().alpha(if (isAvailable) 1f else 0.6f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) PrimaryGreen else SurfaceCardColor),
        color = bgColor,
        shadowElevation = shadowElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isAvailable) { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(time, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtext, fontSize = 13.sp, color = if (isSelected) PrimaryGreenDark else TextSecondaryLight, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
            }

            if (isAvailable) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(price, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NavyDeep)
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryGreen else Color.Transparent)
                            .border(2.dp, if (isSelected) PrimaryGreen else Color.LightGray.copy(0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Rounded.Check, null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            } else {
                Surface(color = Color.Black.copy(0.05f), shape = RoundedCornerShape(6.dp)) {
                    Text("Booked", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}