package com.example.pickleball.ui.screens.match

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material.icons.outlined.SportsBaseball
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pickleball.data.model.Booking
import com.example.pickleball.data.model.PlayerMatchDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.viewmodel.BookingViewModel
import com.google.android.gms.location.LocationServices
import android.location.Location
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object MatchTheme {
    val Primary = Color(0xFF00F684) // Electric Cyan
    val Navy = Color(0xFF050A30)     // Deep Navy
    val CoolGray = Color(0xFFE8EBF0)// Cool Gray
    val SoftMint = Color(0xFFD6FFF3)// Soft Mint
    val White = Color(0xFFFFFFFF)
}

@Composable
fun MatchDetailsScreen(
    matchId: String? = null,
    onBackClick: () -> Unit = {},
    onDepositClick: () -> Unit = {},
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    val matchesState by bookingViewModel.casualMatchesState.collectAsState()
    val rankedMatchesState by bookingViewModel.rankedMatchesState.collectAsState()
    val matchCandidatesState by bookingViewModel.matchCandidatesState.collectAsState()
    val checkInState by bookingViewModel.checkInState.collectAsState()

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    val initiateCheckIn = {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    bookingViewModel.checkIn(matchId!!.toLong(), location.latitude, location.longitude)
                } else {
                    bookingViewModel.checkIn(matchId!!.toLong(), 21.02, 105.85) // mock logic
                }
            }
        } catch(e: SecurityException) {
             bookingViewModel.checkIn(matchId!!.toLong(), 21.02, 105.85)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                      perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            initiateCheckIn()
        }
    }

    LaunchedEffect(matchId) {
        matchId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadMatchCandidates(id)
            if (matchesState !is UiState.Success) bookingViewModel.loadAvailableCasualMatches()
            if (rankedMatchesState !is UiState.Success) bookingViewModel.loadAvailableRankedMatches()
        }
    }

    LaunchedEffect(checkInState) {
        if (checkInState is UiState.Success) {
            Toast.makeText(context, "Checked In Successfully via GPS!", Toast.LENGTH_LONG).show()
            bookingViewModel.resetCheckInState()
        } else if (checkInState is UiState.Error) {
            Toast.makeText(context, (checkInState as UiState.Error).message, Toast.LENGTH_LONG).show()
            bookingViewModel.resetCheckInState()
        }
    }

    val casualMatches = (matchesState as? UiState.Success)?.data ?: emptyList()
    val rankedMatches = (rankedMatchesState as? UiState.Success)?.data ?: emptyList()
    
    val casualDetails = casualMatches.find { it.booking.id.toString() == matchId }
    val rankedDetails = rankedMatches.find { it.booking.id.toString() == matchId }
    val candidates = (matchCandidatesState as? UiState.Success)?.data ?: emptyList()

    val genericBooking = casualDetails?.booking ?: rankedDetails?.booking
    val matchTitle = if (rankedDetails != null) "RANKED MATCH" else "CASUAL MATCH"

    val bookingState by bookingViewModel.bookingState.collectAsState()

    LaunchedEffect(bookingState) {
        if (bookingState is UiState.Success) {
            bookingViewModel.resetBookingState()
            Toast.makeText(context, "Joined Match Successfully!", Toast.LENGTH_SHORT).show()
            onBackClick() // or stay
        }
    }

    Scaffold(
        containerColor = MatchTheme.White,
        topBar = { MatchDetailsTopBar(onBackClick) },
        bottomBar = {
            if (genericBooking != null) {
                MatchDetailsBottomBar(
                    depositAmount = casualDetails?.depositPerPlayer ?: rankedDetails?.depositPerPlayer ?: 0.0,
                    currentPlayers = casualDetails?.currentPlayerCount ?: rankedDetails?.currentPlayerCount ?: candidates.size,
                    requiredPlayers = casualDetails?.requiredPlayerCount ?: rankedDetails?.requiredPlayerCount ?: 4,
                    isLoading = bookingState is UiState.Loading,
                    isCheckInLoading = checkInState is UiState.Loading,
                    onDepositClick = {
                        matchId?.toLongOrNull()?.let { bookingViewModel.joinCasualMatch(it) }
                    },
                    onCheckInClick = {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    }
                )
            }
        }
    ) { paddingValues ->
        if (genericBooking == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MatchTheme.Primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { HeaderInfoCardDynamic(genericBooking, matchTitle) }
                item { MapSection() }
                item { PlayersSectionDynamic(genericBooking, candidates, casualDetails?.currentPlayerCount ?: rankedDetails?.currentPlayerCount ?: candidates.size, casualDetails?.requiredPlayerCount ?: rankedDetails?.requiredPlayerCount ?: 4) }
                item { MatchInfoGridDynamic(genericBooking, casualDetails?.depositPerPlayer ?: rankedDetails?.depositPerPlayer ?: 0.0) }
                item { MatchNotesDynamic(genericBooking.notes) }
                
                if (rankedDetails != null) {
                    item { PostMatchActions(matchId = bookingViewModel, id = matchId!!.toLong(), onReport = {}) }
                }
            }
        }
    }
}

@Composable
fun MatchDetailsTopBar(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(64.dp).background(MatchTheme.White.copy(alpha = 0.95f)).padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp).clip(CircleShape)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MatchTheme.Navy) }
        Text("Match Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
        IconButton(onClick = { }, modifier = Modifier.size(40.dp).clip(CircleShape)) { Icon(Icons.Default.Share, contentDescription = "Share", tint = MatchTheme.Navy) }
    }
}

@Composable
fun HeaderInfoCardDynamic(booking: Booking, matchTitle: String) {
    var dayStr = "TBD"
    var dateStr = "TBD"
    var timeStr = "TBD"
    try {
        if (booking.startTime != null) {
            val ldt = LocalDateTime.parse(booking.startTime)
            dayStr = ldt.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH).uppercase()
            dateStr = ldt.format(DateTimeFormatter.ofPattern("MMM dd"))
            timeStr = ldt.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    } catch (e: Exception) { /* ignore */ }

    Box(modifier = Modifier.fillMaxWidth().shadow(20.dp, RoundedCornerShape(12.dp), ambientColor = MatchTheme.Navy.copy(alpha = 0.05f), spotColor = MatchTheme.Navy.copy(alpha = 0.05f)).clip(RoundedCornerShape(12.dp)).background(MatchTheme.CoolGray).border(1.dp, MatchTheme.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(MatchTheme.SoftMint.copy(alpha = 0.8f), Color.Transparent), center = androidx.compose.ui.geometry.Offset(x = 800f, y = 100f), radius = 500f)))

        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(color = MatchTheme.Primary, shape = RoundedCornerShape(50), modifier = Modifier.padding(bottom = 20.dp), shadowElevation = 2.dp) {
                Text(text = matchTitle, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), letterSpacing = 1.sp)
            }

            Text(text = timeStr, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${dayStr.lowercase().replaceFirstChar { it.uppercase() }}, $dateStr", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 20.dp))

            Surface(color = MatchTheme.White.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.6f))) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.LocationOn, null, tint = MatchTheme.Navy, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${booking.venueName ?: "Unknown"}, ${booking.courtName ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun MapSection() {
    Box(modifier = Modifier.fillMaxWidth().height(128.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, MatchTheme.CoolGray, RoundedCornerShape(12.dp))) {
        AsyncImage(model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBVkNBItZ6jMn7M4TwQNx7aIul19k1-Jy4TkyUkS5ilCR7a9JWZILOQiQGXRemJrkWkJz0l5b2kwK0khj8RjnDmyAmYryuIRf1AtuUpDttUhKgG16zi8X5zJt-jnxJcGaOP2Rp1ABKh-S7jsMvY4G0WKV0axHMmVNdeIiFXaYG02m4A1YEEJkkzCo3S47dcj7yHee71v7JjnWOhYBrjETcSoOPbsu8EKDsoh-j5Vo883FD3ty2vVwqQOBRfaGxAutqxekv9qo4Wn7JU", contentDescription = "Map View", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().background(Color.LightGray))
        Surface(modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).clickable { }, shape = RoundedCornerShape(8.dp), color = MatchTheme.White, border = BorderStroke(1.dp, MatchTheme.CoolGray), shadowElevation = 2.dp) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("View Map", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = MatchTheme.Navy, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun PlayersSectionDynamic(booking: Booking, candidates: List<PlayerMatchDTO>, cur: Int, req: Int) {
    var parsedElo = "N/A"
    booking.notes?.split("|")?.forEach { part ->
        val trimmed = part.trim()
        if (trimmed.startsWith("Elo:")) parsedElo = trimmed.removePrefix("Elo:").trim().toFloatOrNull()?.toInt()?.toString() ?: "N/A"
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Players ($cur/$req)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
            Surface(color = MatchTheme.CoolGray, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, MatchTheme.White)) {
                Text("Avg ELO: $parsedElo", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(alpha = 0.8f), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val teamSize = req / 2
        Card(colors = CardDefaults.cardColors(containerColor = MatchTheme.CoolGray), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.5f))) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(modifier = Modifier.fillMaxHeight().width(6.dp).background(MatchTheme.Primary))
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("TEAM 1", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(alpha = 0.6f), letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    val teamA = candidates.take(teamSize)
                    teamA.forEach { player ->
                        PlayerRow(name = player.fullName ?: "Player", role = player.loyaltyTier ?: "Member", elo = player.currentElo?.toString() ?: "-", imageUrl = "https://i.pravatar.cc/150?u=${player.userId}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    repeat(teamSize - teamA.size) { OpenSlot(); Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().dashedBorder(2.dp, MatchTheme.CoolGray, 12.dp).background(MatchTheme.White, RoundedCornerShape(12.dp)).padding(16.dp)) {
            Column {
                Text("TEAM 2", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(alpha = 0.6f), letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                val teamB = candidates.drop(teamSize)
                teamB.forEach { player ->
                    PlayerRow(name = player.fullName ?: "Player", role = player.loyaltyTier ?: "Member", elo = player.currentElo?.toString() ?: "-", imageUrl = "https://i.pravatar.cc/150?u=${player.userId}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                repeat(teamSize - teamB.size) { OpenSlot(); Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun OpenSlot() {
    Box(modifier = Modifier.fillMaxWidth().dashedBorder(2.dp, MatchTheme.SoftMint, 8.dp).background(MatchTheme.SoftMint.copy(alpha = 0.2f), RoundedCornerShape(8.dp)).clickable { }.padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(MatchTheme.White, CircleShape).border(2.dp, MatchTheme.Primary, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, null, tint = MatchTheme.Primary) }
                Spacer(modifier = Modifier.width(12.dp))
                Column { Text("Open Slot", fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp); Text("Join this team", fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(0.7f), fontSize = 10.sp) }
            }
            Icon(Icons.Default.ChevronRight, null, tint = MatchTheme.Primary)
        }
    }
}

@Composable
fun PlayerRow(name: String, role: String, elo: String, imageUrl: String) {
    Surface(color = MatchTheme.White, shape = RoundedCornerShape(8.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) { AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape).border(1.dp, MatchTheme.CoolGray, CircleShape).background(Color.Gray), contentScale = ContentScale.Crop); Spacer(modifier = Modifier.width(12.dp)); Column { Text(name, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp); Text(role, fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(0.6f), fontSize = 10.sp) } }
            EloBadge(elo)
        }
    }
}

@Composable
fun EloBadge(elo: String) {
    Surface(color = MatchTheme.SoftMint, shape = RoundedCornerShape(6.dp), border = BorderStroke(1.dp, MatchTheme.Primary.copy(alpha = 0.1f))) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp).widthIn(min = 60.dp)) { Text(elo, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy, fontSize = 12.sp); Text("ELO", fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.5f), fontSize = 9.sp) }
    }
}

@Composable
fun MatchInfoGridDynamic(booking: Booking, deposit: Double) {
    var parsedFormat = "Doubles"
    var parsedReferee = if(booking.bookingType == "RANKED") "Yes" else "No"
    booking.notes?.split("|")?.forEach { part ->
        val trimmed = part.trim()
        if (trimmed.startsWith("Format:")) parsedFormat = trimmed.removePrefix("Format:").trim()
        if (trimmed.startsWith("Referee:")) { if (trimmed.removePrefix("Referee:").trim().toBoolean()) parsedReferee = "Yes" }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { InfoCard(icon = Icons.Outlined.Payments, label = "Deposit", value = "$$deposit", modifier = Modifier.weight(1f)); InfoCard(icon = Icons.Outlined.SportsBaseball, label = "Type", value = booking.bookingType ?: "Casual", modifier = Modifier.weight(1f)) }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { InfoCard(icon = Icons.Outlined.Rule, label = "Format", value = parsedFormat, modifier = Modifier.weight(1f)); InfoCard(icon = Icons.Outlined.LocalPolice, label = "Referee", value = parsedReferee, modifier = Modifier.weight(1f)) }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(color = MatchTheme.CoolGray, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.5f)), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Icon(icon, null, tint = MatchTheme.Navy.copy(0.5f), modifier = Modifier.size(24.dp)); Spacer(modifier = Modifier.height(4.dp)); Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy.copy(0.6f), letterSpacing = 0.5.sp); Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy) }
    }
}

@Composable
fun MatchNotesDynamic(notes: String?) {
    var rawNotes = ""
    notes?.split("|")?.forEach { part -> val trimmed = part.trim(); if (trimmed.startsWith("Notes:")) rawNotes = trimmed.removePrefix("Notes:").trim() }
    Column { Text("MATCH NOTES", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)); Surface(color = MatchTheme.White, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.CoolGray), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) { Text(text = rawNotes.ifEmpty { "No specific notes provided by the host." }, style = MaterialTheme.typography.bodyMedium, color = MatchTheme.Navy.copy(alpha = 0.8f), lineHeight = 22.sp, modifier = Modifier.padding(20.dp)) } }
}

@Composable
fun MatchDetailsBottomBar(
    depositAmount: Double,
    currentPlayers: Int,
    requiredPlayers: Int,
    isLoading: Boolean = false,
    isCheckInLoading: Boolean = false,
    onDepositClick: () -> Unit = {},
    onCheckInClick: () -> Unit = {}
) {
    Surface(color = MatchTheme.White.copy(alpha = 0.95f), shadowElevation = 16.dp, border = BorderStroke(1.dp, MatchTheme.CoolGray)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 16.dp).height(56.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val isFull = currentPlayers >= requiredPlayers
            
            if (isFull) {
                Button(onClick = onCheckInClick, enabled = !isCheckInLoading, modifier = Modifier.weight(1f).fillMaxHeight(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.Navy, contentColor = MatchTheme.White)) {
                    if (isCheckInLoading) CircularProgressIndicator(color = MatchTheme.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    else Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Check In (GPS)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold); Text("Must be at venue", fontSize = 10.sp, fontWeight = FontWeight.Normal) }
                }
            } else {
                Button(onClick = onDepositClick, enabled = !isLoading, modifier = Modifier.weight(1f).fillMaxHeight(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.Primary, contentColor = MatchTheme.Navy), elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)) {
                    if (isLoading) CircularProgressIndicator(color = MatchTheme.Navy, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    else Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Deposit to Join", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold); Text("$$depositAmount • Secure Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.7f)) }
                }
            }
            Button(onClick = {}, modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.CoolGray, contentColor = MatchTheme.Navy), contentPadding = PaddingValues(0.dp)) { Icon(Icons.Outlined.Chat, null) }
        }
    }
}

fun Modifier.dashedBorder(width: Dp, color: Color, cornerRadius: Dp) = drawBehind { drawRoundRect(color = color, style = Stroke(width = width.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)), cornerRadius = CornerRadius(cornerRadius.toPx())) }

@Composable
fun PostMatchActions(matchId: BookingViewModel, id: Long, onReport: () -> Unit) {
    var showResultDialog by remember { mutableStateOf(false) }
    var showDisputeDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("POST-MATCH ACTIONS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        Surface(color = MatchTheme.White, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.CoolGray), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { showResultDialog = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.Primary, contentColor = MatchTheme.Navy)) {
                    Text("Submit Match Score (Referee)", fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { matchId.confirmMatchResult(id, true) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.Navy, contentColor = MatchTheme.White)) {
                        Text("Confirm Result")
                    }
                    Button(onClick = { showDisputeDialog = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f), contentColor = MatchTheme.White)) {
                        Text("Dispute")
                    }
                }
            }
        }
    }

    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = { Text("Submit Score") },
            text = { Text("Enter the score to submit the final result to the server.") },
            confirmButton = {
                Button(onClick = {
                    matchId.submitMatchResult(id, 11, 7, "A")
                    showResultDialog = false
                }) { Text("Submit 11-7 (Team A)") }
            },
            dismissButton = {
                TextButton(onClick = { showResultDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showDisputeDialog) {
        AlertDialog(
            onDismissRequest = { showDisputeDialog = false },
            title = { Text("Dispute Match Result") },
            text = { Text("If the reported score was incorrect, you can dispute it.") },
            confirmButton = {
                Button(onClick = {
                    matchId.submitDispute(id, "Incorrect score reported", null)
                    showDisputeDialog = false
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Submit Dispute") }
            },
            dismissButton = {
                TextButton(onClick = { showDisputeDialog = false }) { Text("Cancel") }
            }
        )
    }
}
