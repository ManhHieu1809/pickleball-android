package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pickleball.data.model.Court
import com.example.pickleball.data.model.TimeSlot
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.BookingViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val PrimaryGreen = Color(0xFF00F684) // Electric Cyan
val NavyDark = Color(0xFF050A30)     // Deep Navy
val CoolGrayLight = Color(0xFFE8EBF0)// Cool Gray
val SoftMintLight = Color(0xFFD6FFF3)// Soft Mint
val WhitePure = Color(0xFFFFFFFF)

@Composable
fun CreateMatchScreen(
    courtId: String?,
    slotId: String?,
    date: String?,
    navController: NavController,
    onBackClick: () -> Unit,
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    // --- STATE ---
    var notes by remember { mutableStateOf("") }
    
    val courtState by bookingViewModel.courtDetailState.collectAsState()
    val slotsState by bookingViewModel.slotsState.collectAsState()
    
    // Handle data from navigation arguments OR returned backstack result
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val returnedCourtId by savedStateHandle?.getStateFlow<String?>("returned_court_id", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val returnedSlotId by savedStateHandle?.getStateFlow<String?>("returned_slot_id", null)?.collectAsState() ?: remember { mutableStateOf(null) }
    val returnedDate by savedStateHandle?.getStateFlow<String?>("returned_date", null)?.collectAsState() ?: remember { mutableStateOf(null) }

    var finalCourtId by remember { mutableStateOf(courtId) }
    var finalSlotId by remember { mutableStateOf(slotId) }
    var finalDate by remember { mutableStateOf(date) }

    LaunchedEffect(returnedCourtId, returnedSlotId, returnedDate) {
        if (returnedCourtId != null) finalCourtId = returnedCourtId
        if (returnedSlotId != null) finalSlotId = returnedSlotId
        if (returnedDate != null) finalDate = returnedDate
    }

    LaunchedEffect(finalCourtId, finalDate) {
        finalCourtId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadCourtById(id)
            if (finalDate != null) {
                bookingViewModel.loadAvailableSlots(id, finalDate!!) 
            }
        }
    }

    val bookingState by bookingViewModel.bookingState.collectAsState()
    LaunchedEffect(bookingState) {
        if (bookingState is UiState.Success) {
            bookingViewModel.resetBookingState()
            navController.navigate(Routes.MATCH_CREATED)
        }
    }

    val court = (courtState as? UiState.Success<Court>)?.data
    val slots = (slotsState as? UiState.Success<List<TimeSlot>>)?.data ?: emptyList()
    
    val slotOriginal = slots.find { it.id?.toString() == finalSlotId } 
        ?: slots.find { (it.startTime?.hashCode()?.toLong()?.toString()) == finalSlotId }
        ?: slots.getOrNull(finalSlotId?.toIntOrNull() ?: -1)

    // Formatted Data for UI
    val locationTitle = court?.venueName ?: "Select Venue/Court"
    val locationSubtitle = court?.courtName ?: "Tap to choose location"
    
    val dateDisplay = if (finalDate != null && finalDate != "null" && finalDate != "{date}") {
        try {
            val d = LocalDate.parse(finalDate)
            "${d.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)} ${d.dayOfMonth}"
        } catch(e: Exception) { finalDate }
    } else "MM/DD"
    
    val timeDisplay = if (slotOriginal != null) {
        try {
            val s = LocalTime.parse(slotOriginal.startTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
            val e = LocalTime.parse(slotOriginal.endTime).format(DateTimeFormatter.ofPattern("hh:mm a"))
            "$s - $e"
        } catch (e: Exception) { "${slotOriginal.startTime}" }
    } else "--:--"

    Scaffold(
        containerColor = WhitePure,
        topBar = {
            CreateMatchTopBar(onBackClick)
        },
        bottomBar = {
            CreateMatchBottomBar(
                isLoading = bookingState is UiState.Loading,
                onCreate = { 
                    // Actually create match booking
                    val startStr = slotOriginal?.startTime ?: "00:00:00"
                    val endStr = slotOriginal?.endTime ?: "01:00:00"
                    
                    val formatTime = { t: String ->
                        if (t.length == 5) "$t:00" else t
                    }
                    val fullStart = if (startStr.contains("T")) startStr else "${finalDate}T${formatTime(startStr)}"
                    val fullEnd = if (endStr.contains("T")) endStr else "${finalDate}T${formatTime(endStr)}"
                    
                    val combinedNotes = "Notes: $notes"
                    
                    finalCourtId?.toLongOrNull()?.let { cId ->
                        bookingViewModel.createCasualMatch(
                            courtId = cId,
                            startTime = fullStart,
                            endTime = fullEnd,
                            notes = combinedNotes
                        )
                    } ?: run {
                        // User clicked without selecting a court first. This is empty state
                        navController.navigate(Routes.FIND_COURT)
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
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SectionLabel("LOCATION")
            LocationSelector(
                navController = navController,
                title = locationTitle,
                subtitle = locationSubtitle
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DateTimeInput(
                    label = "DATE",
                    value = dateDisplay ?: "MM/DD",
                    icon = Icons.Default.CalendarToday,
                    modifier = Modifier.weight(1f)
                )
                DateTimeInput(
                    label = "TIME",
                    value = timeDisplay,
                    icon = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f)
                )
            }
            SectionLabel("MATCH INFO")
            MatchInfoDisplay()

            SectionLabel("MATCH RULES & NOTES")
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("e.g. Bring your own balls, court 4 is reserved...", color = NavyDark.copy(0.4f), fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(CoolGrayLight, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Transparent),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = WhitePure,
                    unfocusedContainerColor = CoolGrayLight,
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun CreateMatchTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(CoolGrayLight, RoundedCornerShape(8.dp))
                .border(1.dp, WhitePure.copy(0.5f), RoundedCornerShape(8.dp))
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = NavyDark)
        }

        Text(
            text = "Create Casual Match",
            fontFamily = Lexend,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = NavyDark
        )

        Spacer(modifier = Modifier.size(40.dp)) // Dummy spacer to center title
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = NavyDark.copy(0.9f),
        letterSpacing = 0.5.sp
    )
}

@Composable
fun LocationSelector(navController: NavController, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGrayLight, RoundedCornerShape(12.dp))
            .border(1.dp, WhitePure.copy(0.6f), RoundedCornerShape(12.dp))
            .clickable { navController.navigate(Routes.FIND_COURT)}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyDark)
                Text(subtitle, fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = NavyDark.copy(0.4f))
    }
}

@Composable
fun DateTimeInput(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SectionLabel(label)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = NavyDark.copy(0.4f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                fontFamily = Lexend,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = NavyDark
            )
        }
    }
}

@Composable
fun MatchInfoDisplay() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Format & Players
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight, RoundedCornerShape(12.dp))
                .border(1.dp, WhitePure.copy(0.6f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Doubles Format", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyDark)
                Text("Requires 4 players to start", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
            }
            Surface(
                color = SoftMintLight,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "AUTO",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // Payment Mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight, RoundedCornerShape(12.dp))
                .border(1.dp, WhitePure.copy(0.6f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PieChart, null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Split Cost (25%)", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyDark)
                Text("Each player pays equal share", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
            }
            Surface(
                color = SoftMintLight,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "FIXED",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // ELO Matching
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight.copy(0.5f), RoundedCornerShape(12.dp))
                .border(1.dp, PrimaryGreen.copy(0.3f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AddCircle, null, tint = PrimaryGreen)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "System will automatically find players around your ELO level (+/- 200)",
                fontFamily = Lexend,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = NavyDark.copy(0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun CreateMatchBottomBar(isLoading: Boolean, onCreate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhitePure.copy(0.95f))
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (!isLoading) {
                    onCreate()
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(15.dp, spotColor = PrimaryGreen.copy(0.3f), shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDark, disabledContainerColor = CoolGrayLight)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = NavyDark, modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.AddCircle, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CREATE MATCH",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}