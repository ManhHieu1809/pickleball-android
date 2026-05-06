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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pickleball.data.model.RefereeAssignedMatchDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.viewmodel.RefereeViewModel
val DashSurfaceBackground = Color(0xFFF7F9FF)
val DashSurfaceCardStats = Color(0xFFE8EBF0)
val DashSurfaceCardMatch = Color(0xFFF1F4F9)
val DashTrustScoreBg = Color(0xFFE0F7FF)
val DashCompletedBg = Color(0xFFD6FFF3)
val DashCompletedText = Color(0xFF005228)
val DashPrimaryNeon = Color(0xFF00F684)
val DashTextPrimary = Color(0xFF181C20)
val DashTextSecondary = Color(0xFF3B4B3D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefereeDashboardScreen(
    navController: NavController,
    viewModel: RefereeViewModel
) {
    val profileState by viewModel.refereeProfileState.collectAsState()
    val historyState by viewModel.historyMatchesState.collectAsState()
    val refereeName by viewModel.refereeName.collectAsState()
    var isReady by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.checkRefereeStatus()
        viewModel.loadHistoryMatches()
    }

    Scaffold(
        containerColor = DashSurfaceBackground,
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomNavigation(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            ProfileSection(profileState, refereeName)
            StatsSection(profileState)
            ReadyToggleSection(
                isReady = isReady,
                onReadyChanged = {
                    isReady = it
                    viewModel.updateAvailability(it)
                }
            )
            RecentMatchesSection(historyState)
        }
    }
}

@Composable
fun DashboardTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DashSurfaceBackground.copy(alpha = 0.9f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { navController.popBackStack(Routes.PROFILE, inclusive = false) },
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar (Placeholder)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = "REFEREE",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color(0xFF006D37), // Primary Dark
                letterSpacing = 1.sp
            )
        }

        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .size(40.dp)
        ) {
            Icon(Icons.Rounded.Notifications, contentDescription = "Notifications", tint = DashTextSecondary)
        }
    }
}

@Composable
fun ProfileSection(profileState: UiState<*>, refereeName: String) {
    val profile = (profileState as? UiState.Success<*>)?.data as? com.example.pickleball.data.model.RefereeProfileDTO
    
    val title = if (refereeName.isNotEmpty()) {
        refereeName
    } else {
        profile?.userId?.let { "Referee #$it" } ?: "Referee"
    }

    val score = profile?.trustScore ?: 0

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = DashTextPrimary
        )
        Text(
            text = "LEVEL 3 OFFICIAL",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DashTextSecondary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Trust Score Badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(DashTrustScoreBg)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.VerifiedUser, contentDescription = null, tint = DashTextPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Trust Score: $score", fontWeight = FontWeight.Bold, color = DashTextPrimary, fontSize = 14.sp)
        }
    }
}

@Composable
fun StatsSection(profileState: UiState<*>) {
    val profile = (profileState as? UiState.Success<*>)?.data as? com.example.pickleball.data.model.RefereeProfileDTO
    val totalMatches = profile?.totalMatchesRefereed?.toString() ?: "0"
    val pendingDisputes = profile?.pendingDisputes?.toString() ?: "0"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Total Matches Card
        StatCard(
            modifier = Modifier.weight(1f),
            title = "TOTAL MATCHES",
            value = totalMatches
        )
        // Total Earnings Card
        StatCard(
            modifier = Modifier.weight(1f),
            title = "PENDING DISPUTES",
            value = pendingDisputes
        )
    }
}

@Composable
fun StatCard(modifier: Modifier, title: String, value: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(DashSurfaceCardStats)
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = DashTextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = DashTextPrimary
        )
    }
}

@Composable
fun ReadyToggleSection(
    isReady: Boolean,
    onReadyChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.1f),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, DashSurfaceCardStats, RoundedCornerShape(24.dp))
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Ready for Match", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DashTextPrimary)
            Text(text = "Accepting new assignments", fontSize = 12.sp, color = DashTextSecondary)
        }

        Switch(
            checked = isReady,
            onCheckedChange = onReadyChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = DashPrimaryNeon,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun RecentMatchesSection(historyState: UiState<List<RefereeAssignedMatchDTO>>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Recent Ranked Matches",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = DashTextPrimary
        )

        when (historyState) {
            is UiState.Loading -> {
                CircularProgressIndicator(color = DashPrimaryNeon)
            }
            is UiState.Success -> {
                val matches = historyState.data.take(3)
                if (matches.isEmpty()) {
                    Text("No completed matches yet", color = DashTextSecondary)
                } else {
                    matches.forEach { match ->
                        MatchCard(
                            title = match.booking?.venueName ?: "Ranked Match",
                            subtitle = "${match.booking?.bookingType ?: "RANKED"} • ${match.booking?.startTime ?: "-"}"
                        )
                    }
                }
            }
            is UiState.Error -> {
                Text(historyState.message, color = Color.Red)
            }
            else -> Unit
        }
    }
}

@Composable
fun MatchCard(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DashSurfaceCardMatch)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.SportsTennis, contentDescription = null, tint = DashTextSecondary)
            }
            // Text Content
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DashTextPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = DashTextSecondary)
            }
        }

        // Completed Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(DashCompletedBg)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(text = "Completed", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DashCompletedText)
        }
    }
}

@Composable
fun DashboardBottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isHomeSelected = currentRoute == Routes.REFEREE_DASHBOARD
    val isMatchesSelected = currentRoute == Routes.REFEREE_MATCHES || currentRoute?.startsWith("match_duty/") == true
    val isRulesSelected = currentRoute?.startsWith("dispute_center") == true
    val isProfileSelected = currentRoute == Routes.PROFILE

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                if (isHomeSelected) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(DashPrimaryNeon).padding(horizontal = 20.dp, vertical = 4.dp)) {
                        Icon(Icons.Rounded.Home, contentDescription = "Home", tint = DashTextPrimary)
                    }
                } else {
                    Icon(Icons.Rounded.Home, contentDescription = "Home")
                }
            },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = isHomeSelected,
            onClick = {
                if (!isHomeSelected) {
                    navController.navigate(Routes.REFEREE_DASHBOARD) {
                        popUpTo(Routes.REFEREE_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedTextColor = DashTextPrimary)
        )
        NavigationBarItem(
            icon = {
                if (isMatchesSelected) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(DashPrimaryNeon).padding(horizontal = 20.dp, vertical = 4.dp)) {
                        Icon(Icons.Rounded.SportsTennis, contentDescription = "Matches", tint = DashTextPrimary)
                    }
                } else {
                    Icon(Icons.Rounded.SportsTennis, contentDescription = "Matches")
                }
            },
            label = { Text("Matches", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = isMatchesSelected,
            onClick = {
                if (!isMatchesSelected) {
                    navController.navigate(Routes.REFEREE_MATCHES) {
                        popUpTo(Routes.REFEREE_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedTextColor = DashTextPrimary)
        )
        NavigationBarItem(
            icon = {
                if (isRulesSelected) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(DashPrimaryNeon).padding(horizontal = 20.dp, vertical = 4.dp)) {
                        Icon(Icons.Rounded.Gavel, contentDescription = "Disputes", tint = DashTextPrimary)
                    }
                } else {
                    Icon(Icons.Rounded.Gavel, contentDescription = "Disputes")
                }
            },
            label = { Text("Disputes", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = isRulesSelected,
            onClick = {
                if (!isRulesSelected) {
                    navController.navigate(Routes.disputeCenterRoute(0)) {
                        popUpTo(Routes.REFEREE_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedTextColor = DashTextPrimary)
        )
        NavigationBarItem(
            icon = {
                if (isProfileSelected) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(DashPrimaryNeon).padding(horizontal = 20.dp, vertical = 4.dp)) {
                        Icon(Icons.Rounded.Person, contentDescription = "Profile", tint = DashTextPrimary)
                    }
                } else {
                    Icon(Icons.Rounded.Person, contentDescription = "Profile")
                }
            },
            label = { Text("Profile", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = isProfileSelected,
            onClick = {
                if (!isProfileSelected) {
                    navController.popBackStack(Routes.PROFILE, inclusive = false)
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedTextColor = DashTextPrimary)
        )
    }
}
