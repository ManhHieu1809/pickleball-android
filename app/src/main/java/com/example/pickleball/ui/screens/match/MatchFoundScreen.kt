package com.example.pickleball.ui.screens.match

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.RankedMatchDTO
import com.example.pickleball.viewmodel.RankedMatchViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun MatchFoundScreen(
    navController: NavController,
    viewModel: RankedMatchViewModel = hiltViewModel()
) {
    // Countdown Logic
    var timeLeft by remember { mutableIntStateOf(10) }
    var progress by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        val totalTime = 10000L
        val startTime = System.currentTimeMillis()

        while (timeLeft > 0) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = 1f - (elapsed.toFloat() / totalTime)
            timeLeft = 10 - (elapsed / 1000).toInt()
            delay(50)
        }
        navController.popBackStack()
    }
    
    val matchmakingState by viewModel.matchmakingState.collectAsState()
    val acceptMatchState by viewModel.acceptMatchState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(acceptMatchState) {
        when (acceptMatchState) {
            is UiState.Success -> {
                viewModel.resetAcceptMatchState()
                navController.navigate(Routes.MATCH_STARTING) {
                    popUpTo(Routes.MATCH_FOUND) { inclusive = true }
                }
            }
            is UiState.Error -> {
                Toast.makeText(context, (acceptMatchState as UiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetAcceptMatchState()
            }
            else -> {}
        }
    }
    
    val matchData = (matchmakingState as? UiState.Success<RankedMatchDTO>)?.data
    val currentElo = matchData?.playerCandidates?.firstOrNull()?.currentElo?.toString() ?: "..."
    val courtInfo = matchData?.booking?.venueName ?: "..."
    
    // Chia player candidates thành 2 đội
    val allCandidates = matchData?.playerCandidates ?: emptyList()
    val teamA = if (allCandidates.size >= 2) allCandidates.take(2).mapNotNull { it.fullName } else listOf("YOU", "PARTNER")
    val teamB = if (allCandidates.size >= 4) allCandidates.drop(2).mapNotNull { it.fullName } else listOf("OPPONENT 1", "OPPONENT 2")

    Scaffold(containerColor = NavyBg) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MatchFoundBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ServerBadge()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = (-20).dp)
                ) {
                    // Title đã được tách màu
                    MatchFoundTitle()

                    Spacer(modifier = Modifier.height(30.dp))

                VersusSection(teamA = teamA, teamB = teamB)

                    Spacer(modifier = Modifier.height(40.dp))

                    MatchInfoPanel(courtInfo, currentElo)
                }

                AcceptMatchSection(
                    timeLeft = timeLeft,
                    progress = progress,
                    isLoading = acceptMatchState is UiState.Loading,
                    onAccept = { 
                        val matchId = matchData?.booking?.id
                        if (matchId != null) {
                            viewModel.acceptMatch(matchId.toLong())
                        }
                    },
                    onDecline = {
                        viewModel.stopMatchmaking()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun MatchFoundTitle() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(320.dp, 160.dp)
                .background(PrimaryNeon.copy(0.15f), CircleShape)
                .blur(70.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MATCH",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 52.sp,
                color = Color.White,
                letterSpacing = (-2).sp,
                lineHeight = 52.sp,
                style = TextStyle(
                    shadow = Shadow(color = Color.Black.copy(0.5f), offset = Offset(0f, 4f), blurRadius = 8f)
                )
            )
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .background(PrimaryNeon.copy(0.8f), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(180.dp))
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .background(PrimaryNeon.copy(0.8f), RoundedCornerShape(2.dp))
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = "FOUND",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 52.sp,
                color = PrimaryNeon,
                letterSpacing = (-2).sp,
                lineHeight = 52.sp,
                style = TextStyle(
                    shadow = Shadow(color = PrimaryNeon.copy(0.8f), blurRadius = 20f)
                )
            )
        }
    }
}

@Composable
fun MatchFoundBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(NavyBg.copy(0.8f), NavyBg))))
        Canvas(modifier = Modifier.fillMaxSize().alpha(0.1f)) {
            drawLine(PrimaryNeon, Offset(0f, size.height * 0.2f), Offset(size.width, size.height * 0.15f), 2f)
            drawLine(Color.White, Offset(0f, size.height * 0.8f), Offset(size.width * 0.7f, size.height * 0.85f), 1f)
        }
    }
}

@Composable
fun ServerBadge() {
    Surface(
        color = NavyBg.copy(0.8f),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color.White.copy(0.1f))
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(PrimaryNeon, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text("SERVER: US-EAST", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White.copy(0.6f), letterSpacing = 1.sp)
        }
    }
}

@Composable
fun VersusSection(teamA: List<String>, teamB: List<String>) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(NavyBg, CircleShape)
                .border(2.dp, PrimaryNeon.copy(0.5f), CircleShape)
                .shadow(20.dp, CircleShape, spotColor = PrimaryNeon.copy(0.3f))
                .zIndex(1f)
        ) {
            Text("VS", fontFamily = Lexend, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, fontSize = 20.sp, color = Color.White)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                    Box(modifier = Modifier.size(80.dp).background(PrimaryNeon.copy(0.3f), CircleShape).blur(20.dp))
                    Box(modifier = Modifier.offset(x = 36.dp, y = (-10).dp).size(64.dp).clip(CircleShape).background(Color.Gray).border(2.dp, NavyBg, CircleShape).alpha(0.8f))
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(NavyBg).border(2.dp, PrimaryNeon, CircleShape)) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.fillMaxSize().padding(10.dp))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("YOUR TEAM", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNeon, letterSpacing = 1.sp)
                Text(teamA.joinToString(" + "), fontFamily = Lexend, fontSize = 10.sp, color = Color.White.copy(0.5f))
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                    Box(modifier = Modifier.size(80.dp).background(Color.White.copy(0.1f), CircleShape).blur(20.dp))
                    Box(modifier = Modifier.offset(x = (-36).dp, y = (-10).dp).size(64.dp).clip(CircleShape).background(NavyBg).border(2.dp, Color.White.copy(0.3f), CircleShape)) {
                        Icon(Icons.Default.Person, null, tint = Color.White.copy(0.3f), modifier = Modifier.fillMaxSize().padding(12.dp))
                    }
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(NavyBg).border(2.dp, Color.White.copy(0.3f), CircleShape)) {
                        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(colors = listOf(Color.Gray, NavyBg))), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.SportsTennis, null, tint = Color.White.copy(0.5f), modifier = Modifier.size(32.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("OPPONENTS", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White, letterSpacing = 1.sp)
                Text(teamB.joinToString(" + "), fontFamily = Lexend, fontSize = 10.sp, color = Color.White.copy(0.5f))
            }
        }
    }
}

@Composable
fun MatchInfoPanel(courtInfo: String, currentElo: String) {
    Box(modifier = Modifier.width(280.dp).background(NavyBg.copy(0.6f), RoundedCornerShape(12.dp)).border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(12.dp))) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            InfoItem("MODE", "Doubles")
            VerticalDivider()
            InfoItem("MAP", courtInfo)
            VerticalDivider()
            InfoItem("MMR", currentElo, isHighlight = true)
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Brush.horizontalGradient(colors = listOf(Color.Transparent, PrimaryNeon.copy(0.5f), Color.Transparent))))
    }
}

@Composable
fun InfoItem(label: String, value: String, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.4f), letterSpacing = 1.sp)
        Text(value, fontFamily = Lexend, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if(isHighlight) PrimaryNeon else Color.White)
    }
}

@Composable
fun VerticalDivider() {
    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(0.1f)))
}

@Composable
fun AcceptMatchSection(timeLeft: Int, progress: Float, isLoading: Boolean, onAccept: () -> Unit, onDecline: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .height(60.dp)
                .shadow(30.dp, spotColor = PrimaryNeon.copy(0.4f), shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(if (isLoading) PrimaryNeon.copy(0.5f) else PrimaryNeon)
                .clickable(enabled = !isLoading) { onAccept() }
        ) {
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progress).background(Color.White.copy(0.3f)).align(Alignment.CenterStart))
            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NavyBg, strokeWidth = 2.dp)
                    Text("PROCESSING...", fontFamily = Lexend, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, fontSize = 18.sp, color = NavyBg, letterSpacing = 1.sp)
                    Box(modifier = Modifier.size(32.dp))
                } else {
                    Box(modifier = Modifier.size(40.dp).background(NavyBg.copy(0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Check, null, tint = NavyBg, modifier = Modifier.size(24.dp))
                    }
                    Text("ACCEPT MATCH", fontFamily = Lexend, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, fontSize = 18.sp, color = NavyBg, letterSpacing = 1.sp)
                    Box(modifier = Modifier.size(32.dp).border(2.dp, NavyBg.copy(0.2f), CircleShape), contentAlignment = Alignment.Center) {
                        Text("$timeLeft", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyBg)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("DECLINE MATCH", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White.copy(0.3f), letterSpacing = 2.sp, modifier = Modifier.clickable { onDecline() })
    }
}