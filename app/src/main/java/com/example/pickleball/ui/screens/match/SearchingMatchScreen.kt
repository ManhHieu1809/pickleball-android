package com.example.pickleball.ui.screens.match

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.screens.profile.blur
import com.example.pickleball.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SearchingMatchScreen(
    navController: NavController,
    viewModel: com.example.pickleball.viewmodel.RankedMatchViewModel,
    onCancel: () -> Unit
) {
    // Animation States
    val infiniteTransition = rememberInfiniteTransition(label = "SearchAnim")

    val ripple1 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearOutSlowInEasing)), label = "Ripple1"
    )
    val ripple2 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, delayMillis = 1000, easing = LinearOutSlowInEasing)), label = "Ripple2"
    )
    val ripple3 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, delayMillis = 2000, easing = LinearOutSlowInEasing)), label = "Ripple3"
    )

    val spinRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)), label = "Spin"
    )

    // Timer State
    var waitSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            waitSeconds++
        }
    }

    val matchmakingState by viewModel.matchmakingState.collectAsState()

    LaunchedEffect(matchmakingState) {
        if (matchmakingState is com.example.pickleball.data.model.UiState.Success) {
            // Once match is found, wait 2 seconds before cutting over to MATCH_FOUND
            delay(2000)
            navController.navigate(Routes.MATCH_FOUND) {
                popUpTo(Routes.PRE_MATCH_LOBBY) { inclusive = false }
            }
        }
    }

    Scaffold(containerColor = NavyBg) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            GridMapBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SearchHeader(spinRotation, waitSeconds)
                val playerProfile by viewModel.playerProfileState.collectAsState()
                val currentName = if (playerProfile is com.example.pickleball.data.model.UiState.Success) {
                    val data = (playerProfile as com.example.pickleball.data.model.UiState.Success).data
                    data.fullName ?: "Player ${data.userId}"
                } else "You"

                Box(contentAlignment = Alignment.Center) {
                    AvatarGroup( r1 = ripple1.value, r2 = ripple2.value, r3 = ripple3.value, hostName = currentName)
                }
                SearchFooter(onCancel)
            }
        }
    }
}

// --- SUB COMPONENTS ---

@Composable
fun GridMapBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Gradient nền tối
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.radialGradient(colors = listOf(Color.Transparent, NavyBg), radius = 800f))
        )

        // Vẽ lưới (Canvas)
        Canvas(modifier = Modifier.fillMaxSize().alpha(0.05f)) {
            val step = 40.dp.toPx()
            // Vẽ đường dọc
            for (x in 0..size.width.toInt() step step.toInt()) {
                drawLine(
                    color = Color.White,
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), size.height),
                    strokeWidth = 1f
                )
            }
            // Vẽ đường ngang
            for (y in 0..size.height.toInt() step step.toInt()) {
                drawLine(
                    color = Color.White,
                    start = Offset(0f, y.toFloat()),
                    end = Offset(size.width, y.toFloat()),
                    strokeWidth = 1f
                )
            }
        }

        // Map Blocks (Các hình chữ nhật mờ xoay nghiêng giả lập bản đồ)
        MapBlock(Modifier.align(Alignment.TopStart).offset(40.dp, 60.dp).rotate(-3f))
        MapBlock(Modifier.align(Alignment.CenterEnd).offset((-60).dp, (-40).dp).rotate(2f))
        MapBlock(Modifier.align(Alignment.BottomStart).offset(80.dp, (-80).dp).rotate(-1f))
    }
}

@Composable
fun MapBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(100.dp, 140.dp)
            .background(Color.White.copy(0.02f), RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(0.05f), RoundedCornerShape(8.dp))
    )
}

@Composable
fun SearchHeader(spinRotation: Float, waitSeconds: Int) {
    val minutes = waitSeconds / 60
    val seconds = waitSeconds % 60
    val formattedTime = String.format("%d:%02d", minutes, seconds)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Status Badge
        Surface(
            color = NavyBg.copy(0.6f),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.White.copy(0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spinner Icon
                Canvas(modifier = Modifier.size(14.dp).rotate(spinRotation)) {
                    drawArc(
                        color = PrimaryNeon,
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "MATCHMAKING ACTIVE",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = PrimaryNeon,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "SEARCHING FOR\nOPPONENTS...",
            fontFamily = Lexend,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Estimated Wait: $formattedTime",
            fontFamily = Lexend,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color.White.copy(0.4f),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun RadarRipples(r1: Float, r2: Float, r3: Float) {
    Box(contentAlignment = Alignment.Center) {
        // Vòng tròn Range tĩnh
        Canvas(modifier = Modifier.size(350.dp)) {
            drawCircle(
                color = Color.White.copy(0.1f),
                style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            )
        }
        Text(
            "RANGE: 15KM",
            color = Color.White.copy(0.3f),
            fontSize = 9.sp,
            fontFamily = Lexend,
            modifier = Modifier.offset(y = (-180).dp)
        )

        RippleCircle(r1)
        RippleCircle(r2)
        RippleCircle(r3)
    }
}

@Composable
fun RippleCircle(progress: Float) {
    val scale = 0.5f + (progress * 2f)
    val alpha = (1f - progress) * 0.5f

    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(scale)
            .alpha(alpha)
            .border(1.dp, PrimaryNeon, CircleShape)
    )
}
@Composable
fun AvatarGroup(
    r1: Float,
    r2: Float,
    r3: Float,
    hostName: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            RadarRipples(r1, r2, r3)

            Box(
                modifier = Modifier
                    .offset(x = (-65).dp, y = 12.dp)
                    .scale(0.85f)
                    .alpha(0.8f)
                    .size(80.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
                    .border(1.dp, Color.White.copy(0.1f), CircleShape)
                    .background(NavyBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                )
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .zIndex(2f)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryNeon, Color.White)
                        )
                    )
                    .padding(2.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NavyBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                hostName,
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                " & ",
                color = Color.White.copy(0.4f),
                fontSize = 12.sp
            )
            Text(
                "Searching...",
                fontFamily = Lexend,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.White.copy(0.6f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = Color.White.copy(0.05f),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.White.copy(0.05f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(PrimaryNeon, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "DUO QUEUE",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = PrimaryNeon,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}


@Composable
fun SearchFooter(onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SCANNING LOCAL COURTS...",
            fontFamily = Lexend,
            fontSize = 10.sp,
            color = Color.White.copy(0.3f),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = NavyBg.copy(0.4f),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.White.copy(0.2f))
        ) {
            Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp).alpha(0.6f))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "CANCEL SEARCH",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
        }
    }
}