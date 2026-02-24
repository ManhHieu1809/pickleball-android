package com.example.pickleball.ui.screens.match

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MatchStartingScreen(
    navController: NavController
) {
    // --- Logic đếm ngược ---
    var countdown by remember { mutableIntStateOf(3) }
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val duration = 3000L
        val startTime = System.currentTimeMillis()

        while (progress < 1f) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
            countdown = 3 - (elapsed / 1000).toInt()
            delay(16)
        }

        // Hết giờ -> Chuyển sang màn hình chơi game chính thức
        // navController.navigate(Routes.IN_GAME)
    }

    Scaffold(containerColor = NavyBg) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MatchStartingBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LobbyBadge()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    MatchStartingTitle()

                    Spacer(modifier = Modifier.height(40.dp))
                    PlayersGrid()
                }
                GameLaunchingPanel(countdown, progress)
            }
        }
    }
}

@Composable
fun MatchStartingBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(colors = listOf(NavyBg.copy(0.8f), NavyBg))))
        Canvas(modifier = Modifier.fillMaxSize().alpha(0.1f)) {
            drawLine(PrimaryNeon, Offset(0f, size.height * 0.2f), Offset(size.width, size.height * 0.15f), 2f)
            drawLine(Color.White, Offset(0f, size.height * 0.8f), Offset(size.width * 0.7f, size.height * 0.85f), 1f)
        }
    }
}

@Composable
fun LobbyBadge() {
    Surface(
        color = NavyBg.copy(0.8f),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color.White.copy(0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(6.dp).background(PrimaryNeon, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "LOBBY #8392",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(0.6f),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun MatchStartingTitle() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(300.dp, 150.dp)
                .background(PrimaryNeon.copy(0.15f), CircleShape)
                .blur(60.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MATCH",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 48.sp,
                color = Color.White,
                letterSpacing = (-1).sp,
                lineHeight = 48.sp,
                style = TextStyle(shadow = Shadow(color = Color.Black.copy(0.5f), offset = Offset(0f, 4f), blurRadius = 8f))
            )
            Text(
                text = "STARTING",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 48.sp,
                color = PrimaryNeon,
                letterSpacing = (-1).sp,
                lineHeight = 48.sp,
                style = TextStyle(shadow = Shadow(color = PrimaryNeon.copy(0.8f), blurRadius = 20f))
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier.width(8.dp).height(4.dp).background(PrimaryNeon, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.width(40.dp).height(4.dp).background(Color.White.copy(0.2f), RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.width(8.dp).height(4.dp).background(PrimaryNeon, RoundedCornerShape(2.dp)))
            }
        }
    }
}

@Composable
fun PlayersGrid() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        // VS Circle Center
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                PlayerAvatar(name = "PICKLERICK", isReady = true, delay = 0)
                PlayerAvatar(name = "DUO_PARTNER", isReady = true, delay = 100)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                PlayerAvatar(name = "OPPONENT 1", isReady = true, delay = 200)
                PlayerAvatar(name = "OPPONENT 2", isReady = true, delay = 300)
            }
        }
    }
}

@Composable
fun PlayerAvatar(name: String, isReady: Boolean, delay: Int) {
    val transition = updateTransition(targetState = isReady, label = "CheckPop")
    val scale by transition.animateFloat(
        label = "Scale",
        transitionSpec = { tween(400, delayMillis = delay, easing = FastOutSlowInEasing) }
    ) { state -> if (state) 1f else 0f }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
            // Glow nền
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PrimaryNeon.copy(0.3f), CircleShape)
                    .blur(16.dp)
            )

            // Avatar Container
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(NavyBg)
                    .border(2.dp, PrimaryNeon, CircleShape)
            ) {
                // Placeholder Image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Color.White.copy(0.3f))
                }

                // Overlay tối màu
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.6f)))
            }

            // Check Icon (Pop animation)
            if (isReady) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = PrimaryNeon,
                    modifier = Modifier
                        .size(32.dp)
                        .scale(scale)
                        .shadow(10.dp, CircleShape, spotColor = PrimaryNeon)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            fontFamily = Lexend,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = PrimaryNeon,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun GameLaunchingPanel(countdown: Int, progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyBg.copy(0.6f), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "GAME LAUNCHING",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(0.5f),
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Starting in $countdown...",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 28.sp,
                color = PrimaryNeon,
                style = TextStyle(shadow = Shadow(color = PrimaryNeon.copy(0.8f), blurRadius = 15f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(NavyBg.copy(0.5f), CircleShape)
                    .border(1.dp, Color.White.copy(0.1f), CircleShape)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress) // Thanh chạy từ 0 -> 1
                        .background(PrimaryNeon)
                        .shadow(10.dp, spotColor = PrimaryNeon)
                )
            }
        }
    }
}