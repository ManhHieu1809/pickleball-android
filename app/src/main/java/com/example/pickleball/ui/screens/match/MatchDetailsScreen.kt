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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

object MatchTheme {
    val Primary = Color(0xFF00F684)
    val Navy = Color(0xFF050A30)
    val CoolGray = Color(0xFFE8EBF0)
    val SoftMint = Color(0xFFD6FFF3)
    val White = Color(0xFFFFFFFF)
}

@Composable
fun MatchDetailsScreen(
    onBackClick: () -> Unit = {},
    onDepositClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MatchTheme.White,
        topBar = { MatchDetailsTopBar(onBackClick) },
        bottomBar = { MatchDetailsBottomBar(onDepositClick = onDepositClick) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp), // Extra padding for scroll
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { HeaderInfoCard() }
            item { MapSection() }
            item { PlayersSection() }
            item { MatchInfoGrid() }
            item { MatchNotes() }
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
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MatchTheme.Navy
            )
        }

        Text(
            text = "Match Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MatchTheme.Navy
        )

        IconButton(
            onClick = { /* Share Action */ },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = MatchTheme.Navy
            )
        }
    }
}

@Composable
fun HeaderInfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = MatchTheme.Navy.copy(alpha = 0.05f),
                spotColor = MatchTheme.Navy.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(MatchTheme.CoolGray)
            .border(1.dp, MatchTheme.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        // Mesh Gradient Simulation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MatchTheme.SoftMint.copy(alpha = 0.8f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(x = 800f, y = 100f),
                        radius = 500f
                    )
                )
        )

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge
            Surface(
                color = MatchTheme.Primary,
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 20.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    text = "RANKED MATCH",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MatchTheme.Navy,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "10:00 AM",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MatchTheme.Navy,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sat, Oct 24 • 90 min",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MatchTheme.Navy.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Location Badge
            Surface(
                color = MatchTheme.White.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MatchTheme.Navy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Venice Beach Courts, Court 4",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MatchTheme.Navy
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
            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBVkNBItZ6jMn7M4TwQNx7aIul19k1-Jy4TkyUkS5ilCR7a9JWZILOQiQGXRemJrkWkJz0l5b2kwK0khj8RjnDmyAmYryuIRf1AtuUpDttUhKgG16zi8X5zJt-jnxJcGaOP2Rp1ABKh-S7jsMvY4G0WKV0axHMmVNdeIiFXaYG02m4A1YEEJkkzCo3S47dcj7yHee71v7JjnWOhYBrjETcSoOPbsu8EKDsoh-j5Vo883FD3ty2vVwqQOBRfaGxAutqxekv9qo4Wn7JU", // Map Image URL
            contentDescription = "Map View",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().background(Color.LightGray)
        )

        // Map Overlay
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clickable { },
            shape = RoundedCornerShape(8.dp),
            color = MatchTheme.White,
            border = BorderStroke(1.dp, MatchTheme.CoolGray),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Map",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MatchTheme.Navy
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    tint = MatchTheme.Navy,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun PlayersSection() {
    Column {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Players (2/4)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MatchTheme.Navy
            )

            Surface(
                color = MatchTheme.CoolGray,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MatchTheme.White)
            ) {
                Text(
                    text = "Avg ELO: 1485",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MatchTheme.Navy.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Team A (Filled)
        Card(
            colors = CardDefaults.cardColors(containerColor = MatchTheme.CoolGray),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.5f))
        ) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                // Vertical Bar
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(MatchTheme.Primary)
                )

                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(
                        text = "TEAM A (FULL)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MatchTheme.Navy.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    PlayerRow(
                        name = "Sarah M.",
                        role = "Pro Member",
                        elo = "1520",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBke_uLYzHaUNQ762VrMNHGy9gdbt8oI_x0-H8HyoQn-iLjYMiR2SEAlfBpX5TfjMsINvg4A1BqPqzXJeDP9NAT6qDdpUHG08U_P7otbkUO3TxdLF7qomRGewVcotO9RyBPL4ZCfxrvF-SkpCC8JwOAQs6yI-uW6yHQ79wDSxab5EYOuybLjWu2DmLvtDs0qP8GQDtVBWV2qSairEotTfnkVdVSosWujR4hzlXSGRv5yh2SooVl2bzb6aWdwyo76stRMdQRF8Ipsq93"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PlayerRow(
                        name = "David K.",
                        role = "Rookie",
                        elo = "1450",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDTnhmg6vbxTGC8ap4pcQQQqAaUORtw_otTnYkNWd8-bTqX9pUJfxkHz85JD-arwA242CCVUimjwlMjdRikCDlPo_uMPJCuSqPmIjyp8v7m-RXm4BCyXi3In5Su2gK5kBY9CPfbktJrbN34wXxGIo5jtZriadem0j5eaMX896gnTYuossiN1hmDcP_7V3T1DVdTcnzZa-0rc_MTXICCOZuo785bFBb3MOdbEsdw6QLdqQbDjTw5PmhLGrOV9cg1vexhwUFvhwYmZmzS"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Team B (Dashed)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(2.dp, MatchTheme.CoolGray, 12.dp)
                .background(MatchTheme.White, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "TEAM B (OPEN)",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MatchTheme.Navy.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Player B1
                Surface(
                    color = MatchTheme.CoolGray,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDJBJHq8EHCEnzF7btw4uBcFc8ZHW41ziHaJqB9HFzYdccy0gSw12Gjw-TkXEc2OzGYYXkh0z42bAYMXHWB2h8tTBm8J34wXwapyu0HvCWGbz9qeyOMCL9Fxl2OoVgEswRnV3UAof3sdqRLAmfwG7yXGd6Mus7BQG9syKdD3mws00V9eer9UAeNqhVVyR3VfwnSU-Zk8pM5NV1pGbfmi_GqfgiMfecfwHgZIN6IY9eQvusTyD0Lna-4AgxY2asW2U516KSMBsxlSkCy",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MatchTheme.White, CircleShape)
                                    .background(Color.Gray),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Jessica L.", fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp)
                        }
                        EloBadge("1410")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Open Slot
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dashedBorder(2.dp, MatchTheme.SoftMint, 8.dp)
                        .background(MatchTheme.SoftMint.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .clickable { }
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MatchTheme.White, CircleShape)
                                    .border(2.dp, MatchTheme.Primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
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
        }
    }
}

@Composable
fun PlayerRow(name: String, role: String, elo: String, imageUrl: String) {
    Surface(
        color = MatchTheme.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, MatchTheme.CoolGray, CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
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
    Surface(
        color = MatchTheme.SoftMint,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, MatchTheme.Primary.copy(alpha = 0.1f))
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp).widthIn(min = 60.dp)
        ) {
            Text(elo, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy, fontSize = 12.sp)
            Text("ELO", fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.5f), fontSize = 9.sp)
        }
    }
}

@Composable
fun MatchInfoGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(
                icon = Icons.Outlined.Payments,
                label = "Entry Fee",
                value = "$15.00",
                modifier = Modifier.weight(1f)
            )
            InfoCard(
                icon = Icons.Outlined.SportsBaseball,
                label = "Ball Type",
                value = "Dura Fast 40",
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(
                icon = Icons.Outlined.Rule,
                label = "Format",
                value = "Best of 3",
                modifier = Modifier.weight(1f)
            )
            InfoCard(
                icon = Icons.Outlined.LocalPolice,
                label = "Referee",
                value = "Self-Ref",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = MatchTheme.CoolGray,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MatchTheme.White.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = MatchTheme.Navy.copy(0.5f), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy.copy(0.6f), letterSpacing = 0.5.sp)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy)
        }
    }
}

@Composable
fun MatchNotes() {
    Column {
        Text(
            "MATCH NOTES",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MatchTheme.Navy,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Surface(
            color = MatchTheme.White,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MatchTheme.CoolGray),
            shadowElevation = 2.dp, // shadow-soft
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Standard competitive rules apply. Please arrive 10 minutes early for warm-up. Winner stays on court for next casual rotation if time permits.",
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
    onDepositClick: () -> Unit = {}
) {
    Surface(
        color = MatchTheme.White.copy(alpha = 0.95f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, MatchTheme.CoolGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 16.dp) // SafeArea
                .height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Deposit Button
            Button(
                onClick = onDepositClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatchTheme.Primary,
                    contentColor = MatchTheme.Navy
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp) // shadow-glow effect simulation
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Deposit to Join", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    Text("$15.00 • Secure Pay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.7f))
                }
            }

            // Chat Button
            Button(
                onClick = {},
                modifier = Modifier
                    .size(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatchTheme.CoolGray,
                    contentColor = MatchTheme.Navy
                ),
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
