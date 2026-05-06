package com.example.pickleball.ui.screens.referee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.viewmodel.RefereeViewModel

val SurfaceLowest = Color(0xFFFFFFFF)
val SurfaceLow = Color(0xFFF1F4F9)
val PrimaryNeon = Color(0xFF00F684)
val PrimaryDark = Color(0xFF006D37)
val ErrorRed = Color(0xFFBA1A1A)
val WarningYellow = Color(0xFFF9A825)
val TextPrimary = Color(0xFF181C20)
val TextSecondary = Color(0xFF3B4B3D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefereeHubScreen(
    navController: NavController,
    viewModel: RefereeViewModel
) {
    val profileState by viewModel.refereeProfileState.collectAsState()
    val isPending by viewModel.isPendingApproval.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkRefereeStatus()
    }

    // Redirect to Dashboard if already an active referee
    LaunchedEffect(profileState) {
        val state = profileState
        if (state is UiState.Success && state.data?.isActive == true) {
            navController.navigate(Routes.REFEREE_DASHBOARD) {
                popUpTo(Routes.REFEREE_HUB) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = SurfaceLowest,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Referee Hub",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = PrimaryDark)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Navigate to Profile */ },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(SurfaceLow)
                    ) {
                        Icon(Icons.Rounded.Person, contentDescription = "Profile", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SurfaceLowest
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            DashboardBottomNavigation(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (profileState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        color = PrimaryNeon,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    // Main Scrollable Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                            .padding(bottom = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Hero Icon
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(SurfaceLowest)
                                .border(1.dp, SurfaceLow, CircleShape)
                                .shadow(8.dp, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SportsScore,
                                contentDescription = "Whistle",
                                tint = PrimaryDark,
                                modifier = Modifier.size(56.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Status Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(SurfaceLow)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CURRENT STATUS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val statusText = if (isPending) "Awaiting Approval" else "Not Certified"
                            val statusIcon = if (isPending) Icons.Rounded.HourglassEmpty else Icons.Rounded.Cancel
                            val statusColor = if (isPending) WarningYellow else ErrorRed

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(statusIcon, contentDescription = statusText, tint = statusColor)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = statusText,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Text Content
                        Text(
                            text = if (isPending) "Verification in Progress" else "Officiate Ranked Matches and earn tokens via AI certification.",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isPending) "Your certification results have been submitted. An administrator will review your application shortly. You will be notified once your status is updated." 
                                   else "Step onto the court with authority. Our rapid AI certification process validates your rule knowledge and unlocks paid officiating opportunities in your local area.",
                            fontSize = 16.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Info Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Rounded.Token,
                                title = "Earn Tokens",
                                subtitle = "Paid per match"
                            )
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Rounded.CheckCircle,
                                title = "Get Verified",
                                subtitle = "AI Assessed"
                            )
                        }
                    }

                    // Floating Action Button Overlay
                    Button(
                        onClick = { 
                            if (!isPending) {
                                navController.navigate(Routes.AI_QUIZ) 
                            }
                        },
                        enabled = !isPending,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryNeon,
                            disabledContainerColor = SurfaceLow
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(if (isPending) 0.dp else 12.dp, RoundedCornerShape(50), ambientColor = PrimaryNeon)
                    ) {
                        Text(
                            text = if (isPending) "PENDING REVIEW" else "START AI CERTIFICATION TEST",
                            color = if (isPending) TextSecondary else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (!isPending) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier = Modifier, icon: ImageVector, title: String, subtitle: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceLowest)
            .border(1.dp, SurfaceLow, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = PrimaryDark, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary, textAlign = TextAlign.Center)
        Text(text = subtitle, fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
    }
}


