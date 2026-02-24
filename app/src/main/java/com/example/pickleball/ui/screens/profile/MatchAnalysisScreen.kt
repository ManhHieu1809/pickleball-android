package com.example.pickleball.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.theme.*

@Composable
fun MatchAnalysisScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            MatchAnalysisTopBar(onBackClick)
        },
        bottomBar = {
            ShareButton()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Hero Score Card
            HeroScoreCard()

            // 2. Statistics Section
            StatisticsSection()

            // 3. Heatmap Section
            HeatmapSection()

            // Spacer for bottom bar
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- Components ---

@Composable
fun MatchAnalysisTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 16.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(CoolGray.copy(0.5f), CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "Match Analysis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun HeroScoreCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = NavyDeep.copy(0.3f))
            .clip(RoundedCornerShape(20.dp))
            .background(NavyDeep)
    ) {
        // Decorative Gradients
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .background(PrimaryGreen.copy(0.1f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .background(PrimaryGreen.copy(0.05f), CircleShape)
        )

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Victory & Info
            Text(
                text = "VICTORY",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = PrimaryGreen,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color.White.copy(0.1f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.White.copy(0.1f))
                ) {
                    Text(
                        "RANKED",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Oct 24 • 45m", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(0.5f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Players & Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player 1 (You)
                PlayerAvatar(
                    name = "You",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA_7XBGLLNjMMu-cgMr2MCE5Cc7P2H6Ylg8ZLUcxETb1in9slG7tlw-B9hvy0Rg8cSZfYlXGGYh4AT5zG4QMJAiV1AXD1lMc3mv9QNUKYeIouugvcALeuxhTaBYGRI3ypJdApQwnEfGNKgKtQfHilSBNCAOdOQI0WvWRCkjpT1Cea0XDKXiN57BiZSCYIlhHzzpnF7LUMzSDd65qOQAK6khWEvkkdS2q2ZpysKT67EjEtzmbrC7iLwrXjAHLiF8PQJsJvrMi5WqSMha",
                    scoreChange = "+24",
                    isWinner = true
                )

                // Score
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "2 - 0",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-2).sp
                    )
                    Text(
                        "FINAL SCORE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.4f),
                        letterSpacing = 1.sp
                    )
                }

                // Player 2 (Opponent)
                PlayerAvatar(
                    name = "Marcus T.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDHEouXg02cMhBhtfEsVp4s59QKgNk3li2qeHCkmCpz0FCkOuWwxMyXtq4gkVoQJW9HaXfCeHM4ncQ-3pQwxYR1bjYMArSnH04aUsuLE0MqBZZZ8oDhbUC1Jr1WsjV-fEoNUKKYd2Pe0Jd0ERSwpiaRdN3nrIfNd0ypjtHSem443hvxEUOo3U_EELH_bSvNQ3e_m-k-TkIw-UEK8DrD8R5UnLnm298VckHrWxr635_vsVU3FxH3ol8X2DBlCkrLEx6qWBbYZcyFBWAY",
                    scoreChange = "-18",
                    isWinner = false
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.White.copy(0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            // Set Scores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SetScoreItem("SET 1", "11-9")
                Spacer(modifier = Modifier.width(32.dp))
                SetScoreItem("SET 2", "11-8")
            }
        }
    }
}

@Composable
fun PlayerAvatar(name: String, imageUrl: String, scoreChange: String, isWinner: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, if (isWinner) PrimaryGreen else Color.White.copy(0.1f), CircleShape)
                    .then(if(!isWinner) Modifier.graphicsLayer { alpha = 0.8f } else Modifier) // Dim loser
            )

            // Score Badge
            Surface(
                color = if(isWinner) PrimaryGreen else CoolGray,
                shape = RoundedCornerShape(50),
                modifier = Modifier.offset(y = 8.dp),
                border = BorderStroke(1.dp, NavyDeep)
            ) {
                Text(
                    text = scoreChange,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = NavyDeep,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if(isWinner) Color.White else Color.White.copy(0.8f))
    }
}

@Composable
fun SetScoreItem(label: String, score: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.4f), letterSpacing = 1.sp)
        Text(score, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun StatisticsSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Match Statistics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.BarChart, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Service Accuracy Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CoolGray.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SERVICE ACCURACY", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f))
                    Icon(Icons.Default.SportsBaseball, null, tint = NavyDeep.copy(0.2f), modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                StatProgressBar("You", 0.92f, PrimaryGreen)
                Spacer(modifier = Modifier.height(12.dp))
                StatProgressBar("Marcus T.", 0.84f, NavyDeep.copy(0.2f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Stats
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Winners
            StatGridItem(
                label = "WINNERS",
                mainValue = "18",
                subValue = "vs 12",
                progress1 = 0.6f,
                progress2 = 0.4f,
                modifier = Modifier.weight(1f)
            )
            // Unforced Errors
            StatGridItem(
                label = "UNFORCED ERRORS",
                mainValue = "5",
                subValue = "vs 9",
                progress1 = 0.35f,
                progress2 = 0.65f,
                subValueColor = Color(0xFFEF4444), // Red for opponent errors (bad for them, good for me context?) or just red highlight
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Longest Rally
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CoolGray.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("LONGEST RALLY", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f))
                    Text("32 Shots", fontSize = 24.sp, fontWeight = FontWeight.Black, color = NavyDeep, letterSpacing = (-1).sp)
                }
                Box(
                    modifier = Modifier.size(40.dp).background(Color.White, CircleShape).border(1.dp, NavyDeep.copy(0.05f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Timer, null, tint = NavyDeep.copy(0.3f))
                }
            }
        }
    }
}

@Composable
fun StatProgressBar(label: String, progress: Float, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(label == "You") NavyDeep else NavyDeep.copy(0.6f))
            Text("${(progress * 100).toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = if(label == "You") NavyDeep else NavyDeep.copy(0.6f))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(10.dp).background(Color.White, CircleShape).clip(CircleShape)
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(progress).background(color, CircleShape)
            )
        }
    }
}

@Composable
fun StatGridItem(
    label: String,
    mainValue: String,
    subValue: String,
    progress1: Float,
    progress2: Float,
    subValueColor: Color = NavyDeep.copy(0.4f),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f))
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(mainValue, fontSize = 30.sp, fontWeight = FontWeight.Black, color = NavyDeep, modifier = Modifier.alignByBaseline() )
                Spacer(modifier = Modifier.width(4.dp))
                Text(subValue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = subValueColor, modifier = Modifier.alignByBaseline())
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape)) {
                Box(modifier = Modifier.weight(progress1).fillMaxHeight().background(PrimaryGreen))
                Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.White))
                Box(modifier = Modifier.weight(progress2).fillMaxHeight().background(NavyDeep.copy(0.1f)))
            }
        }
    }
}

@Composable
fun HeatmapSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Shot Heatmap", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.Grain, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(NavyDeep)
                .border(1.dp, NavyDeep.copy(0.1f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Court Lines Drawing (Simplified)
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
                    .border(2.dp, Color.White.copy(0.3f))
            ) {
                // Center Line
                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.White.copy(0.3f)).align(Alignment.Center))
                // Kitchen Lines
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(0.3f)).align(Alignment.Center).offset(y = (-60).dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(0.3f)).align(Alignment.Center).offset(y = 60.dp))
                // Net
                Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color.White.copy(0.6f)).align(Alignment.Center).shadow(10.dp, spotColor = Color.White))
            }

            // Heat Spots (Blurred Circles simulation with Gradient)
            Box(
                modifier = Modifier
                    .offset(x = (-40).dp, y = 40.dp)
                    .size(80.dp)
                    .background(Brush.radialGradient(colors = listOf(PrimaryGreen.copy(0.4f), Color.Transparent)))
            )
            Box(
                modifier = Modifier
                    .offset(x = 50.dp, y = (-60).dp)
                    .size(100.dp)
                    .background(Brush.radialGradient(colors = listOf(PrimaryGreen.copy(0.3f), Color.Transparent)))
            )

            // Legend
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                color = NavyDeep.copy(0.8f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.White.copy(0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).background(PrimaryGreen, CircleShape).shadow(4.dp, CircleShape, spotColor = PrimaryGreen))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("IMPACT ZONE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.7f))
                }
            }
        }
    }
}

@Composable
fun ShareButton() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(0.9f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Box(modifier = Modifier.padding(20.dp).navigationBarsPadding()) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Share Stats Analysis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.IosShare, null, modifier = Modifier.size(20.dp))
            }
        }
    }
}