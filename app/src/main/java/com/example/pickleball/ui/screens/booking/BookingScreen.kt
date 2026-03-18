package com.example.pickleball.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.pickleball.data.model.Court
import com.example.pickleball.data.model.TimeSlot
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.BookingViewModel
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookingScreen(
    courtId: String?,
    navController: NavController,
    onBackClick: () -> Unit,
    bookingViewModel: BookingViewModel = hiltViewModel()
){
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSlot by remember { mutableStateOf<TimeSlotData?>(null) }
    var courtPriceBase by remember { mutableStateOf("20 VND") }

    LaunchedEffect(courtId) {
        courtId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadCourtById(id)
            bookingViewModel.loadAvailableSlots(id, selectedDate.format(DateTimeFormatter.ISO_DATE))
        }
    }

    val courtState by bookingViewModel.courtDetailState.collectAsState()
    val slotsState by bookingViewModel.slotsState.collectAsState()

    val court = (courtState as? UiState.Success<Court>)?.data
    if (court != null) {
        courtPriceBase = formatPriceVND(court.priceAmount)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            BookingTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            BookingBottomBar(
                totalPrice = selectedSlot?.priceOrLabel ?: courtPriceBase, 
                navController = navController,
                isEnabled = selectedSlot != null,
                onProceedClick = {
                    val dateFormatted = selectedDate.format(DateTimeFormatter.ISO_DATE)
                    val passingCourtId = courtId ?: "0"
                    val passingSlotId = selectedSlot?.id?.toString() ?: "0"
                    navController.navigate("payment_confirmation/$passingCourtId/$passingSlotId/$dateFormatted")
                },
                onCreateMatchClick = {
                    val dateFormatted = selectedDate.format(DateTimeFormatter.ISO_DATE)
                    val passingCourtId = courtId ?: "0"
                    val passingSlotId = selectedSlot?.id?.toString() ?: "0"
                    
                    try {
                        val createMatchEntry = navController.getBackStackEntry("create_match")
                        createMatchEntry.savedStateHandle["returned_court_id"] = passingCourtId
                        createMatchEntry.savedStateHandle["returned_slot_id"] = passingSlotId
                        createMatchEntry.savedStateHandle["returned_date"] = dateFormatted
                        navController.popBackStack("create_match", inclusive = false)
                    } catch (e: Exception) {
                        navController.navigate("create_match/$passingCourtId/$passingSlotId/$dateFormatted")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .background(
                       Brush.radialGradient(
                           colors = listOf(
                               AccentMint.copy(alpha = 0.6f),
                               Color.Transparent
                           ),
                           center = androidx.compose.ui.geometry.Offset(1000f, 0f),
                            radius = 500f
                       )
                   )
           ) {
               Column {
                   MonthHeaderSection(monthYear = selectedDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + selectedDate.year)
                   DateStripSection(selectedDate = selectedDate, onDateSelected = { newDate ->
                       selectedDate = newDate
                       selectedSlot = null
                       courtId?.toLongOrNull()?.let { id ->
                           bookingViewModel.loadAvailableSlots(id, newDate.format(DateTimeFormatter.ISO_DATE))
                       }
                   })
               }
           }
            CourtInfoCard(court)

            when (slotsState) {
                is UiState.Loading -> {
                     Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                          CircularProgressIndicator(color = PrimaryGreen)
                     }
                }
                is UiState.Success -> {
                    val slots = (slotsState as UiState.Success).data
                    if (slots.isEmpty()) {
                         Text("No available slots for this date.", modifier = Modifier.padding(16.dp), color = TextSecondaryLight)
                    } else {
                        val currentTime = LocalTime.now()
                        val isToday = selectedDate == LocalDate.now()

                        val mappedSlots = slots.mapIndexed { index, it ->
                            val slotIdFallback = it.id ?: it.startTime?.hashCode()?.toLong() ?: index.toLong()
                            
                            var status = SlotStatus.AVAILABLE
                            if (it.isBooked == true || it.isAvailable == false) {
                                status = SlotStatus.BOOKED
                            } else if (isToday) {
                                try {
                                    val slotTime = LocalTime.parse(it.startTime)
                                    if (slotTime.isBefore(currentTime)) {
                                        status = SlotStatus.DISABLED
                                    }
                                } catch (e: Exception) {}
                            }
                            
                            if (status == SlotStatus.AVAILABLE && selectedSlot?.id == slotIdFallback) {
                                status = SlotStatus.SELECTED
                            }

                            TimeSlotData(
                                id = slotIdFallback,
                                time = formatTimeRange(it.startTime, it.endTime),
                                priceOrLabel = formatPriceVND(it.priceAmount ?: court?.priceAmount),
                                status = status,
                                originalData = it
                            )
                        }

                        val morningSlots = mappedSlots.filter { parseToHour(it.originalData?.startTime) < 12 }
                        val afternoonSlots = mappedSlots.filter { parseToHour(it.originalData?.startTime) in 12..17 }
                        val eveningSlots = mappedSlots.filter { parseToHour(it.originalData?.startTime) >= 18 }

                        val onSlotSelected: (TimeSlotData) -> Unit = { clickedSlot ->
                            if (clickedSlot.status == SlotStatus.AVAILABLE || clickedSlot.status == SlotStatus.SELECTED) {
                                selectedSlot = if (selectedSlot?.id == clickedSlot.id) null else clickedSlot
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 24.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            if (morningSlots.isNotEmpty()) {
                                TimeSlotSection(
                                    title = "Morning",
                                    icon = Icons.Default.WbTwilight,
                                    slots = morningSlots,
                                    onSlotClick = onSlotSelected
                                )
                            }
                            if (afternoonSlots.isNotEmpty()) {
                                TimeSlotSection(
                                    title = "Afternoon",
                                    icon = Icons.Default.WbSunny,
                                    slots = afternoonSlots,
                                    onSlotClick = onSlotSelected
                                )
                            }
                            if (eveningSlots.isNotEmpty()) {
                                TimeSlotSection(
                                    title = "Evening",
                                    icon = Icons.Default.Bedtime,
                                    slots = eveningSlots,
                                    onSlotClick = onSlotSelected
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                     Text("Failed to load slots: ${(slotsState as UiState.Error).message}", modifier = Modifier.padding(16.dp), color = Color.Red)
                }
                else -> {}
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun formatTimeRange(start: String?, end: String?): String {
    return try {
        val s = start?.let { LocalTime.parse(it).format(DateTimeFormatter.ofPattern("HH:mm")) } ?: "N/A"
        val e = end?.let { LocalTime.parse(it).format(DateTimeFormatter.ofPattern("HH:mm")) } ?: "N/A"
        "$s - $e"
    } catch (e: Exception) {
        "${start ?: "N/A"} - ${end ?: "N/A"}"
    }
}

fun formatPriceVND(price: Double?, defaultFallback: Double = 0.0): String {
    val amount = price ?: defaultFallback
    return String.format(Locale("vi", "VN"), "%,.0f VND", amount)
}

fun parseToHour(timeStr: String?): Int {
    if (timeStr == null) return 0
    return try {
        LocalTime.parse(timeStr).hour
    } catch (e: Exception) {
        0
    }
}

enum class SlotStatus{AVAILABLE, BOOKED, SELECTED, DISABLED}
data class TimeSlotData(val id: Long, val time: String, val priceOrLabel: String, val status: SlotStatus, val originalData: TimeSlot?)

@Composable
fun BookingTopBar(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
            }
            Text(
                text = "Select Time",
                style = MaterialTheme.typography.titleMedium, // Lexend
                fontWeight = FontWeight.Bold,
                color = NavyDeep
            )
            Spacer(modifier = Modifier.width(48.dp)) // Dummy spacer to center title
        }
    }
}

@Composable
fun MonthHeaderSection(monthYear: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = monthYear,
            style = MaterialTheme.typography.headlineSmall, // Lexend
            color = NavyDeep
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { }
        ) {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.labelMedium,
                color = NavyDeep
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = NavyDeep,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun DateStripSection(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val days = (0..6).map { LocalDate.now().plusDays(it.toLong()) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        days.forEach { dateObj ->
            DateItem(
                day = dateObj.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                date = dateObj.dayOfMonth.toString(),
                isSelected = dateObj == selectedDate,
                onClick = { onDateSelected(dateObj) }
            )
        }
    }
}

@Composable
fun DateItem(day: String, date: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if(isSelected) PrimaryGreen else SurfaceCardColor
    val textColor = if(isSelected) NavyDeep else NavyDeep
    val dayColor = if(isSelected) NavyDeep.copy(0.9f) else NavyDeep.copy(0.7f)
    val shadowElevation = if(isSelected) 8.dp else 0.dp

    Column(
        modifier = Modifier
            .width(56.dp)
            .height(64.dp)
            .shadow(shadowElevation, RoundedCornerShape(8.dp), spotColor = PrimaryGreen.copy(0.5f))
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .border(
                width = if (isSelected) 1.dp else 1.dp,
                color = if (isSelected) PrimaryGreen else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = dayColor
        )
        Text(
            text = date,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            color = textColor
        )
    }
}

@Composable
fun CourtInfoCard(court: Court?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = court?.id?.let { "https://picsum.photos/200/200?${it}" } 
                    ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuDMUxe4iR1t-QYx8zyG1BCbxlrkwqX884KnnGTyY6nuyK_Dx7V_S7nmhjWXYiq8eS_wn8npCPr3VYBO1IvSxe-Kevc-Inp7xKS6lxECmHPFjiCRQ3SVEd2tqo4lVE3TedNl63L6VeUdJfvSPKHvR_Qgpiut41xjBzZQS6UGKz0N_TbXu0OrE4bpfJvJbUpK10GXqn62t9Oz3pVPGSZm1uIp_VxupKmEDPnf1VSsbkSeKRHNBeh8rMlREgEdPRp_vSm5fPtRBdhEEyZr",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = court?.venueName ?: "Riverside Courts",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = court?.courtName ?: "Court 3 • Hard Surface",
                    style = MaterialTheme.typography.bodySmall,
                    color = NavyDeep.copy(0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (court?.isActive != false) "Available" else "Maintenance",
                        style = MaterialTheme.typography.labelSmall,
                        color = NavyDeep,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatPriceVND(court?.priceAmount) + "/hr",
                    style = MaterialTheme.typography.labelLarge,
                    color = NavyDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Change",
                    style = MaterialTheme.typography.labelSmall,
                    color = NavyDeep,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun TimeSlotSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, slots: List<TimeSlotData>, onSlotClick: (TimeSlotData) -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NavyDeep,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                letterSpacing = 1.sp
            )
        }

        val chunkedSlots = slots.chunked(3)
        chunkedSlots.forEachIndexed { index, rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSlots.forEach { slot ->
                    Box(modifier = Modifier.weight(1f)) {
                        TimeSlotCard(slot, onSlotClick)
                    }
                }
                if (rowSlots.size < 3) {
                    repeat(3 - rowSlots.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            if (index < chunkedSlots.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TimeSlotCard(data: TimeSlotData, onSlotClick: (TimeSlotData) -> Unit) {
    val isSelected = data.status == SlotStatus.SELECTED
    val isBookedOrDisabled = data.status == SlotStatus.BOOKED || data.status == SlotStatus.DISABLED

    val bgColor = if (isSelected) AccentMint else SurfaceCardColor
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val textColor = if (isBookedOrDisabled) NavyDeep.copy(0.5f) else NavyDeep
    val priceColor = if (isBookedOrDisabled) NavyDeep.copy(0.5f) else NavyDeep.copy(0.7f)
    val textDecoration = if (isBookedOrDisabled) TextDecoration.LineThrough else null

    Box(
        modifier = Modifier.height(60.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = !isBookedOrDisabled) { onSlotClick(data) },
            shape = RoundedCornerShape(8.dp),
            color = bgColor,
            border = BorderStroke(if(isSelected) 2.dp else 1.dp, borderColor),
            shadowElevation = if(isSelected) 4.dp else 0.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = data.time,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textDecoration = textDecoration
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = data.priceOrLabel,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if(isSelected) NavyDeep else priceColor
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(20.dp)
                    .background(PrimaryGreen, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = NavyDeep,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun BookingBottomBar(totalPrice: String, navController: NavController, isEnabled: Boolean, onProceedClick: () -> Unit, onCreateMatchClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(0.95f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, SurfaceCardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(0.4f)) {
                Text(
                    text = "Total amount",
                    style = MaterialTheme.typography.labelSmall,
                    color = NavyDeep,
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = totalPrice,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 18.sp,
                        color = NavyDeep
                    )
                    Text(
                        text = "/1h",
                        style = MaterialTheme.typography.bodySmall,
                        color = NavyDeep.copy(0.7f),
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onProceedClick,
                    enabled = isEnabled,
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = NavyDeep,
                        disabledContainerColor = Color.LightGray
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Book Private", style = MaterialTheme.typography.labelMedium, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(14.dp))
                }
                
                OutlinedButton(
                    onClick = onCreateMatchClick,
                    enabled = isEnabled,
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NavyDeep,
                    ),
                    border = BorderStroke(1.dp, if(isEnabled) PrimaryGreen else Color.LightGray)
                ) {
                    Text("Open Match", style = MaterialTheme.typography.labelMedium, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}