package com.example.pickleball.ui.screens.referee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.pickleball.data.model.UiState
import com.example.pickleball.viewmodel.RefereeViewModel

val MatchSurfaceBackground = Color(0xFFF7F9FF)
val MatchPrimaryDarkText = Color(0xFF050A30)
val MatchPrimaryNeon = Color(0xFF00F684)
val MatchTextSecondary = Color(0xFF64748B)

val SurfaceCardHeader = Color(0xFFF1F4F9) 
val SurfaceCardTeam = Color(0xFFE5E8ED)

val LiveRed = Color(0xFFBA1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchCenterScreen(
    navController: NavController,
    viewModel: RefereeViewModel,
    matchId: Long
) {
    val selectedMatch by viewModel.selectedMatch.collectAsState()
    val upcomingState by viewModel.upcomingMatchesState.collectAsState()
    val historyState by viewModel.historyMatchesState.collectAsState()
    val submitState by viewModel.matchResultState.collectAsState()

    var teamAScore by remember { mutableIntStateOf(0) }
    var teamBScore by remember { mutableIntStateOf(0) }
    var selectedTeam by remember { mutableStateOf("Team A") }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val checkInState by viewModel.checkInState.collectAsState()

    LaunchedEffect(checkInState) {
        if (checkInState is UiState.Success) {
            Toast.makeText(context, "GPS Check-in Successful", Toast.LENGTH_SHORT).show()
            viewModel.resetCheckInState()
        } else if (checkInState is UiState.Error) {
            Toast.makeText(context, (checkInState as UiState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetCheckInState()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineGranted || coarseGranted) {
            try {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            viewModel.submitCheckIn(location.latitude, location.longitude)
                        } else {
                            Toast.makeText(context, "Could not get location. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: SecurityException) {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(matchId) {
        if (selectedMatch?.rankedMatchId != matchId) {
            viewModel.loadUpcomingMatches()
            viewModel.loadHistoryMatches()
        }
    }

    LaunchedEffect(upcomingState, historyState, matchId, selectedMatch?.rankedMatchId) {
        if (selectedMatch?.rankedMatchId == matchId) return@LaunchedEffect
        val upcoming = (upcomingState as? UiState.Success)?.data.orEmpty()
        val history = (historyState as? UiState.Success)?.data.orEmpty()
        val match = (upcoming + history).firstOrNull { it.rankedMatchId == matchId }
        if (match != null) {
            viewModel.selectMatch(match)
        }
    }

    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) {
            viewModel.resetMatchResultState()
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = MatchSurfaceBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MATCH CENTER",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MatchPrimaryDarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = MatchPrimaryDarkText)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "More", tint = MatchPrimaryDarkText)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MatchSurfaceBackground)
            )
        },
        bottomBar = { DashboardBottomNavigation(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val booking = selectedMatch?.booking
            val players = selectedMatch?.playerCandidates ?: emptyList()
            val team1Name = if (players.isNotEmpty()) {
                val half = (players.size + 1) / 2
                players.take(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
            } else "Team A"

            val team2Name = if (players.size > 1) {
                val half = (players.size + 1) / 2
                players.drop(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
            } else "Team B"

            MatchInfoHeader(
                title = booking?.venueName ?: "Ranked Match #$matchId",
                subtitle = booking?.startTime ?: "Waiting for match details",
                isCheckingIn = checkInState is UiState.Loading,
                onCheckInClick = {
                    val hasFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    val hasCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    
                    if (hasFineLocation || hasCoarseLocation) {
                        try {
                            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                                .addOnSuccessListener { location ->
                                    if (location != null) {
                                        viewModel.submitCheckIn(location.latitude, location.longitude)
                                    } else {
                                        Toast.makeText(context, "Could not get location", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Location service error", Toast.LENGTH_SHORT).show()
                                }
                        } catch (e: SecurityException) {
                            Toast.makeText(context, "Permission error", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            )
            ScoreboardSection(
                team1Name = team1Name,
                team2Name = team2Name,
                teamAScore = teamAScore,
                teamBScore = teamBScore,
                onTeamAPlus = { teamAScore++ },
                onTeamBPlus = { teamBScore++ },
                onTeamAMinus = { if (teamAScore > 0) teamAScore-- },
                onTeamBMinus = { if (teamBScore > 0) teamBScore-- }
            )
            WinnerSelectionSection(
                team1Name = team1Name,
                team2Name = team2Name,
                selectedTeam = selectedTeam,
                onTeamSelected = { selectedTeam = it }
            )
            Button(
                onClick = {
                    viewModel.submitSelectedMatchResult(
                        teamAScore = teamAScore,
                        teamBScore = teamBScore,
                        winningTeam = if (selectedTeam == "Team A") "A" else "B"
                    )
                },
                enabled = submitState !is UiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = MatchPrimaryNeon),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(8.dp, RoundedCornerShape(50), ambientColor = MatchPrimaryNeon)
            ) {
                if (submitState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MatchPrimaryDarkText, strokeWidth = 2.dp)
                } else {
                    Text("Submit Match Result", color = MatchPrimaryDarkText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MatchInfoHeader(
    title: String, 
    subtitle: String,
    isCheckingIn: Boolean,
    onCheckInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceCardHeader)
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MatchPrimaryDarkText
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(LiveRed)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MatchTextSecondary
            )
        }

        Button(
            onClick = onCheckInClick,
            enabled = !isCheckingIn,
            colors = ButtonDefaults.buttonColors(containerColor = MatchPrimaryNeon),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (isCheckingIn) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MatchPrimaryDarkText, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Rounded.MyLocation, contentDescription = null, tint = MatchPrimaryDarkText, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("GPS Check-in Required", color = MatchPrimaryDarkText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ScoreboardSection(
    team1Name: String,
    team2Name: String,
    teamAScore: Int,
    teamBScore: Int,
    onTeamAPlus: () -> Unit,
    onTeamBPlus: () -> Unit,
    onTeamAMinus: () -> Unit,
    onTeamBMinus: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TeamScoreCard(
            modifier = Modifier.weight(1f),
            teamName = team1Name,
            score = teamAScore,
            onPlus = onTeamAPlus,
            onMinus = onTeamAMinus
        )
        TeamScoreCard(
            modifier = Modifier.weight(1f),
            teamName = team2Name,
            score = teamBScore,
            onPlus = onTeamBPlus,
            onMinus = onTeamBMinus
        )
    }
}

@Composable
fun TeamScoreCard(
    modifier: Modifier,
    teamName: String,
    score: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(SurfaceCardTeam)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Overlapping Avatars
        Row(
            horizontalArrangement = Arrangement.spacedBy(-12.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            AvatarPlaceholder()
            AvatarPlaceholder()
        }

        Text(
            text = teamName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = MatchPrimaryDarkText
        )

        Text(
            text = score.toString(),
            fontSize = 64.sp,
            fontWeight = FontWeight.Black,
            color = MatchPrimaryDarkText,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Huge "+" Button
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MatchPrimaryNeon)
                .clickable { onPlus() }
                .shadow(4.dp, CircleShape, ambientColor = MatchPrimaryNeon),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Increase Score", tint = MatchPrimaryDarkText, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "-" Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable { onMinus() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Remove, contentDescription = "Decrease Score", tint = MatchPrimaryDarkText)
        }
    }
}

@Composable
fun AvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .border(2.dp, SurfaceCardTeam, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Rounded.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun WinnerSelectionSection(
    team1Name: String,
    team2Name: String,
    selectedTeam: String,
    onTeamSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Declare Winner",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MatchPrimaryDarkText,
            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(SurfaceCardTeam)
                .padding(6.dp)
        ) {
            // Team A Toggle
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (selectedTeam == "Team A") MatchPrimaryNeon else Color.Transparent)
                    .clickable { onTeamSelected("Team A") }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = team1Name,
                    fontWeight = FontWeight.Bold,
                    color = MatchPrimaryDarkText,
                    fontSize = 14.sp
                )
            }

            // Team B Toggle
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (selectedTeam == "Team B") MatchPrimaryNeon else Color.Transparent)
                    .clickable { onTeamSelected("Team B") }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = team2Name,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTeam == "Team B") MatchPrimaryDarkText else MatchTextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}
