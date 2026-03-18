package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pickleball.data.model.CasualMatchDTO
import com.example.pickleball.data.model.PlayerMatchDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.viewmodel.BookingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object MatchTheme {
    val Primary = Color(0xFF00F684)
    val Navy = Color(0xFF050A30)
    val CoolGray = Color(0xFFE8EBF0)
    val SoftMint = Color(0xFFD6FFF3)
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
    val matchCandidatesState by bookingViewModel.matchCandidatesState.collectAsState()

    LaunchedEffect(matchId) {
        matchId?.toLongOrNull()?.let { id ->
            bookingViewModel.loadMatchCandidates(id)
            if (matchesState !is UiState.Success) {
                bookingViewModel.loadAvailableCasualMatches()
            }
        }
    }

    val matches = (matchesState as? UiState.Success<List<CasualMatchDTO>>)?.data ?: emptyList()
    val matchDetails = matches.find { it.booking.id.toString() == matchId }
    val candidates = (matchCandidatesState as? UiState.Success<List<PlayerMatchDTO>>)?.data ?: emptyList()

    val bookingState by bookingViewModel.bookingState.collectAsState()

    LaunchedEffect(bookingState) {
        if (bookingState is UiState.Success) {
            bookingViewModel.resetBookingState()
            onBackClick()
        }
    }

    Scaffold(
        containerColor = MatchTheme.White,
        topBar = { MatchDetailsTopBar(onBackClick) },
        bottomBar = {
            if (matchDetails != null) {
                MatchDetailsBottomBar(
                    matchDetails = matchDetails,
                    isLoading = bookingState is UiState.Loading,
                    onDepositClick = {
                        matchId?.toLongOrNull()?.let {
                            bookingViewModel.joinCasualMatch(it)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        if (matchDetails == null) {
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
                item { HeaderInfoCard(matchDetails) }
                item { MapSection() }
                item { PlayersSection(matchDetails, candidates) }
                item { MatchInfoGrid(matchDetails) }
                item { MatchNotes(matchDetails) }
            }
        }
    }
}

@Composable
fun MatchDetailsTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MatchTheme.White.copy(alpha = 0.95f))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.dp).clip(CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MatchTheme.Navy)
        }
        Text("Match Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
        IconButton(
            onClick = { },
            modifier = Modifier.size(40.dp).clip(CircleShape)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", tint = MatchTheme.Navy)
        }
    }
}

@Composable
fun HeaderInfoCard(match: CasualMatchDTO) {
    var dayStr = "TBD"
    var dateStr = "TBD"
    var timeStr = "TBD"
    try {
        if (match.booking.startTime != null) {
            val ldt = LocalDateTime.parse(match.booking.startTime)
            dayStr = ldt.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH).uppercase()
            dateStr = ldt.format(DateTimeFormatter.ofPattern("MMM dd"))
            timeStr = ldt.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    } catch (e: Exception) { /* ignore */ }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(20.dp, RoundedCornerShape(12.dp), ambientColor = MatchTheme.Navy.copy(alpha = 0.05f), spotColor = MatchTheme.Navy.copy(alpha = 0.05f))
            .clip(RoundedCornerShape(12.dp))
            .background(MatchTheme.CoolGray)
            .border(1.dp, MatchTheme.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MatchTheme.SoftMint.copy(alpha = 0.8f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(x = 800f, y = 100f),
                        radius = 500f
                    )
                )
        )

        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = MatchTheme.Primary,
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 20.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "CASUAL MATCH",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MatchTheme.Navy,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = timeStr,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MatchTheme.Navy,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${dayStr.lowercase().replaceFirstChar { it.uppercase() }}, $dateStr",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MatchTheme.Navy.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Surface(
                color = MatchTheme.White.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.LocationOn, null, tint = MatchTheme.Navy, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${match.booking.venueName ?: "Unknown"}, ${match.booking.courtName ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MatchTheme.Navy,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MapSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MatchTheme.CoolGray, RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBVkNBItZ6jMn7M4TwQNx7aIul19k1-Jy4TkyUkS5ilCR7a9JWZILOQiQGXRemJrkWkJz0l5b2kwK0khj8RjnDmyAmYryuIRf1AtuUpDttUhKgG16zi8X5zJt-jnxJcGaOP2Rp1ABKh-S7jsMvY4G0WKV0axHMmVNdeIiFXaYG02m4A1YEEJkkzCo3S47dcj7yHee71v7JjnWOhYBrjETcSoOPbsu8EKDsoh-j5Vo883FD3ty2vVwqQOBRfaGxAutqxekv9qo4Wn7JU",
            contentDescription = "Map View",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().background(Color.LightGray)
        )

        Surface(
            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).clickable { },
            shape = RoundedCornerShape(8.dp),
            color = MatchTheme.White,
            border = BorderStroke(1.dp, MatchTheme.CoolGray),
            shadowElevation = 2.dp
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("View Map", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = MatchTheme.Navy, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun PlayersSection(match: CasualMatchDTO, candidates: List<PlayerMatchDTO>) {
    val req = match.requiredPlayerCount ?: 4
    val cur = match.currentPlayerCount ?: candidates.size

    var parsedElo = "N/A"
    match.booking.notes?.split("|")?.forEach { part ->
        val trimmed = part.trim()
        if (trimmed.startsWith("Elo:")) {
            parsedElo = trimmed.removePrefix("Elo:").trim().toFloatOrNull()?.toInt()?.toString() ?: "N/A"
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Players ($cur/$req)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
            Surface(color = MatchTheme.CoolGray, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, MatchTheme.White)) {
                Text("Avg ELO: $parsedElo", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(alpha = 0.8f), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val teamSize = req / 2

        // Team 1
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
                    repeat(teamSize - teamA.size) {
                        OpenSlot()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Team 2
        Box(
            modifier = Modifier.fillMaxWidth().dashedBorder(2.dp, MatchTheme.CoolGray, 12.dp).background(MatchTheme.White, RoundedCornerShape(12.dp)).padding(16.dp)
        ) {
            Column {
                Text("TEAM 2", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(alpha = 0.6f), letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                val teamB = candidates.drop(teamSize)
                teamB.forEach { player ->
                    PlayerRow(name = player.fullName ?: "Player", role = player.loyaltyTier ?: "Member", elo = player.currentElo?.toString() ?: "-", imageUrl = "https://i.pravatar.cc/150?u=${player.userId}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                repeat(teamSize - teamB.size) {
                    OpenSlot()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun OpenSlot() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(2.dp, MatchTheme.SoftMint, 8.dp)
            .background(MatchTheme.SoftMint.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .clickable { }
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(MatchTheme.White, CircleShape).border(2.dp, MatchTheme.Primary, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, null, tint = MatchTheme.Primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Open Slot", fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp)
                    Text("Join this team", fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(0.7f), fontSize = 10.sp)
                }
            }
            Icon(Icons.Default.ChevronRight, null, tint = MatchTheme.Primary)
        }
    }
}

@Composable
fun PlayerRow(name: String, role: String, elo: String, imageUrl: String) {
    Surface(color = MatchTheme.White, shape = RoundedCornerShape(8.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape).border(1.dp, MatchTheme.CoolGray, CircleShape).background(Color.Gray), contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp)
                    Text(role, fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(0.6f), fontSize = 10.sp)
                }
            }
            EloBadge(elo)
        }
    }
}

@Composable
fun EloBadge(elo: String) {
    Surface(color = MatchTheme.SoftMint, shape = RoundedCornerShape(6.dp), border = BorderStroke(1.dp, MatchTheme.Primary.copy(alpha = 0.1f))) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp).widthIn(min = 60.dp)) {
            Text(elo, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy, fontSize = 12.sp)
            Text("ELO", fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.5f), fontSize = 9.sp)
        }
    }
}

@Composable
fun MatchInfoGrid(match: CasualMatchDTO) {
    var parsedFormat = "Doubles"
    var parsedReferee = "No"
    match.booking.notes?.split("|")?.forEach { part ->
        val trimmed = part.trim()
        if (trimmed.startsWith("Format:")) parsedFormat = trimmed.removePrefix("Format:").trim()
        if (trimmed.startsWith("Referee:")) {
            if (trimmed.removePrefix("Referee:").trim().toBoolean()) parsedReferee = "Yes"
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(icon = Icons.Outlined.Payments, label = "Deposit", value = "$${match.depositPerPlayer?.toString() ?: "0.00"}", modifier = Modifier.weight(1f))
            InfoCard(icon = Icons.Outlined.SportsBaseball, label = "Type", value = "Public", modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(icon = Icons.Outlined.Rule, label = "Format", value = parsedFormat, modifier = Modifier.weight(1f))
            InfoCard(icon = Icons.Outlined.LocalPolice, label = "Referee", value = parsedReferee, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(color = MatchTheme.CoolGray, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.5f)), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = MatchTheme.Navy.copy(0.5f), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy.copy(0.6f), letterSpacing = 0.5.sp)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy)
        }
    }
}

@Composable
fun MatchNotes(match: CasualMatchDTO) {
    var rawNotes = ""
    match.booking.notes?.split("|")?.forEach { part ->
        val trimmed = part.trim()
        if (trimmed.startsWith("Notes:")) rawNotes = trimmed.removePrefix("Notes:").trim()
    }

    Column {
        Text("MATCH NOTES", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MatchTheme.Navy, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        Surface(color = MatchTheme.White, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MatchTheme.CoolGray), shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = rawNotes.ifEmpty { "No specific notes provided by the host." },
                style = MaterialTheme.typography.bodyMedium,
                color = MatchTheme.Navy.copy(alpha = 0.8f),
                lineHeight = 22.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun MatchDetailsBottomBar(
    matchDetails: CasualMatchDTO,
    isLoading: Boolean = false,
    onDepositClick: () -> Unit = {}
) {
    Surface(color = MatchTheme.White.copy(alpha = 0.95f), shadowElevation = 16.dp, border = BorderStroke(1.dp, MatchTheme.CoolGray)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 16.dp).height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val isFull = (matchDetails.currentPlayerCount ?: 0) >= (matchDetails.requiredPlayerCount ?: 4)
            val btnText = if (isFull) "Match Full" else if (isLoading) "Joining..." else "Deposit to Join"

            Button(
                onClick = onDepositClick,
                enabled = !isFull && !isLoading,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFull) MatchTheme.CoolGray else MatchTheme.Primary,
                    contentColor = if (isFull) MatchTheme.Navy.copy(0.4f) else MatchTheme.Navy,
                    disabledContainerColor = MatchTheme.CoolGray,
                    disabledContentColor = MatchTheme.Navy.copy(0.4f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isFull) 0.dp else 6.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MatchTheme.Navy, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(btnText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        if (!isFull) {
                            Text("$${matchDetails.depositPerPlayer?.toString() ?: "0.00"} • Secure Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.7f))
                        }
                    }
                }
            }

            Button(
                onClick = {},
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MatchTheme.CoolGray, contentColor = MatchTheme.Navy),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Outlined.Chat, null)
            }
        }
    }
}

fun Modifier.dashedBorder(width: Dp, color: Color, cornerRadius: Dp) = drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}
