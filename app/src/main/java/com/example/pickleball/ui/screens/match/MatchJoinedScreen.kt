package com.example.pickleball.ui.screens.match

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// --- 1. THEME REUSE ---
// Vẫn sử dụng object MatchTheme từ các file trước
// Đảm bảo bạn đã có object này trong project
// object MatchTheme { ... }

@Composable
fun MatchJoinedScreen(
    onViewMatchesClick: () -> Unit = {},
    onHomeClick: () -> Unit = {}
) {
    // Animation State
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Delay slightly for smooth transition
        delay(100)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    LaunchedEffect(Unit) {
        delay(100)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
    }

    Scaffold(
        containerColor = MatchTheme.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Background Gradient
            MeshGradientBackground()

            // 2. Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Success Burst Header (Animated)
                Box(
                    modifier = Modifier
                        .scale(scale.value)
                        .alpha(alpha.value)
                ) {
                    SuccessHeader()
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Ticket Card
                ConfirmationTicket()

                Spacer(modifier = Modifier.weight(1f))

                // Bottom Buttons
                ActionButtons(onViewMatchesClick, onHomeClick)
            }
        }
    }
}

// --- SUB-COMPOSABLES ---

@Composable
fun MeshGradientBackground() {
    // Replicating the CSS mesh-gradient
    Box(modifier = Modifier.fillMaxSize().background(MatchTheme.White)) {
        // Top Right - Soft Mint
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MatchTheme.SoftMint, Color.Transparent),
                        center = Offset(x = 1000f, y = 100f), // Approximate 90% 10%
                        radius = 800f
                    )
                )
        )
        // Top Left - Cool Gray
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MatchTheme.CoolGray, Color.Transparent),
                        center = Offset(x = 100f, y = 100f),
                        radius = 600f
                    )
                )
        )
        // Bottom Center - Primary Green low opacity
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(MatchTheme.Primary.copy(alpha = 0.15f), Color.Transparent),
                        center = Offset(x = 500f, y = 1500f),
                        radius = 900f
                    )
                )
        )
    }
}

@Composable
fun SuccessHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Glow Effect Container
        Box(contentAlignment = Alignment.Center) {
            // Outer Glow (Blurred circle simulation)
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(MatchTheme.Primary.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )

            // Main Icon Circle
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .shadow(15.dp, CircleShape, spotColor = MatchTheme.Primary.copy(alpha = 0.5f))
                    .background(MatchTheme.SoftMint, CircleShape)
                    .border(4.dp, MatchTheme.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = MatchTheme.Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Match Joined!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MatchTheme.Navy,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You're all set for the game.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MatchTheme.Navy.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ConfirmationTicket() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = MatchTheme.Navy.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(MatchTheme.CoolGray)
            .border(1.dp, MatchTheme.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
    ) {
        // Decorative Shape (Top Right corner)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(100.dp)
                .offset(x = 20.dp, y = (-20).dp)
                .background(MatchTheme.Primary.copy(alpha = 0.05f), CircleShape)
        )

        Column(modifier = Modifier.padding(24.dp)) {
            // Top Row: Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MatchTheme.Primary,
                    shape = RoundedCornerShape(6.dp),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = "RANKED MATCH",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MatchTheme.Navy,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp
                    )
                }

                Surface(
                    color = MatchTheme.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = MatchTheme.Navy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CONFIRMED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MatchTheme.Navy,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time & Date
            Text(
                text = "10:00 AM",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MatchTheme.Navy
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MatchTheme.Navy.copy(0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Saturday, Oct 24 • 90 min",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MatchTheme.Navy.copy(0.8f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider
            HorizontalDivider(color = MatchTheme.Navy.copy(alpha = 0.05f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(20.dp))

            // Details List
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TicketInfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "LOCATION",
                    title = "Venice Beach Courts",
                    subtitle = "Court 4"
                )
                TicketInfoRow(
                    icon = Icons.Default.SportsBaseball,
                    label = "LEVEL",
                    title = "Advanced (4.0+)",
                    subtitle = null
                )
            }
        }
    }
}

@Composable
fun TicketInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    title: String,
    subtitle: String?
) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MatchTheme.White,
            shadowElevation = 1.dp,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = MatchTheme.Navy, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MatchTheme.Navy.copy(0.6f),
                letterSpacing = 0.5.sp
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MatchTheme.Navy
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MatchTheme.Navy.copy(0.7f)
                )
            }
        }
    }
}

@Composable
fun ActionButtons(onViewMatches: () -> Unit, onHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Primary Button
        Button(
            onClick = onViewMatches,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MatchTheme.Primary,
                contentColor = MatchTheme.Navy
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text("View My Matches", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
        }

        // Secondary Button
        TextButton(
            onClick = onHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MatchTheme.Navy
            )
        ) {
            Text("Back to Home", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
