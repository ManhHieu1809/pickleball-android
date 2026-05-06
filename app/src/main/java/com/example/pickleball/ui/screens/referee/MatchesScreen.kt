package com.example.pickleball.ui.screens.referee

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.data.model.RefereeAssignedMatchDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.viewmodel.RefereeViewModel

val SurfaceBackground = Color(0xFFF7F9FF)
val CardWhite = Color(0xFFFFFFFF)
val CardGray = Color(0xFFF1F4F9)
val PrimaryDarkText = Color(0xFF181C20)
val ErrorBg = Color(0xFFFFDAD6)
val DividerGray = Color(0xFFE0E3E8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    navController: NavController,
    viewModel: RefereeViewModel
) {
    var showUpcoming by remember { mutableStateOf(true) }
    val upcomingState by viewModel.upcomingMatchesState.collectAsState()
    val historyState by viewModel.historyMatchesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUpcomingMatches()
        viewModel.loadHistoryMatches()
    }

    Scaffold(
        containerColor = SurfaceBackground,
        topBar = {
            Column(modifier = Modifier.background(SurfaceBackground)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Matches",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = PrimaryDarkText
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = PrimaryDarkText)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = PrimaryNeon)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceBackground)
                )

                // Tabs Layout
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(modifier = Modifier.clickable { showUpcoming = true }) {
                        Text(
                            text = "Upcoming",
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            color = if (showUpcoming) PrimaryDarkText else TextSecondary.copy(alpha = 0.5f)
                        )
                        if (showUpcoming) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(6.dp)
                                    .width(60.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(PrimaryNeon)
                            )
                        }
                    }
                    Text(
                        text = "History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = if (!showUpcoming) PrimaryDarkText else TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .clickable { showUpcoming = false }
                    )
                }
            }
        },
        bottomBar = {
            DashboardBottomNavigation(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val state = if (showUpcoming) upcomingState else historyState

            when (state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(color = PrimaryNeon)
                }
                is UiState.Error -> {
                    Text(text = state.message, color = ErrorRed)
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text(
                            text = if (showUpcoming) "No upcoming matches" else "No match history",
                            color = TextSecondary
                        )
                    } else {
                        state.data.forEach { match ->
                            RefereeMatchListCard(
                                match = match,
                                onClick = {
                                    viewModel.selectMatch(match)
                                    navController.navigate(Routes.matchDutyRoute(match.rankedMatchId))
                                }
                            )
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun RefereeMatchListCard(
    match: RefereeAssignedMatchDTO,
    onClick: () -> Unit
) {
    val booking = match.booking
    val title = booking?.venueName ?: "Ranked Match #${match.rankedMatchId}"
    val startTime = booking?.startTime ?: "-"
    
    val players = match.playerCandidates ?: emptyList()
    val team1Name = if (players.isNotEmpty()) {
        val half = (players.size + 1) / 2
        players.take(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
    } else "Team A"

    val team2Name = if (players.size > 1) {
        val half = (players.size + 1) / 2
        players.drop(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
    } else "Team B"

    Box(modifier = Modifier.clickable(onClick = onClick)) {
        StandardMatchCard(
            category = match.matchStatus ?: "UNKNOWN",
            time = startTime,
            period = "",
            court = title,
            team1 = team1Name,
            team2 = team2Name
        )
    }
}

@Composable
fun HeroMatchCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(alpha = 0.05f))
            .clip(RoundedCornerShape(32.dp))
            .background(CardWhite)
            .padding(24.dp)
    ) {
        // Status Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ErrorBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ErrorRed))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "STARTING SOON", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ErrorRed, letterSpacing = 1.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(CardGray)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "MEN'S DOUBLES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Time & Location
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = "10:00", fontSize = 48.sp, fontWeight = FontWeight.Black, color = PrimaryDarkText)
            Text(text = "AM", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        }
        Text(text = "Today • Central Park Court 4", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        // Teams Layout
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(CardGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("T1", fontWeight = FontWeight.Bold, color = TextSecondary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Johnson / Smith", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = PrimaryDarkText)
            }

            // Vertical connection line
            Box(
                modifier = Modifier
                    .padding(start = 19.dp, top = 4.dp, bottom = 4.dp)
                    .width(2.dp)
                    .height(16.dp)
                    .background(DividerGray)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(CardGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("T2", fontWeight = FontWeight.Bold, color = TextSecondary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Davis / Miller", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = PrimaryDarkText)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button
        Button(
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Icon(Icons.Rounded.QrCodeScanner, contentDescription = null, tint = PrimaryDarkText)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Check-in Teams", color = PrimaryDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun StandardMatchCard(category: String, time: String, period: String, court: String, team1: String, team2: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardGray)
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(DividerGray.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = category, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = time, fontSize = 32.sp, fontWeight = FontWeight.Black, color = PrimaryDarkText)
            Text(text = period, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        }
        Text(text = court, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFC0E8DD)))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = team1, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PrimaryDarkText)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(DividerGray))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = team2, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PrimaryDarkText)
            }
        }
    }
}

