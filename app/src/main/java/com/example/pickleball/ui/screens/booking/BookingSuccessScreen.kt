package com.example.pickleball.ui.screens.booking

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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*



@Composable
fun BookingSuccessScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            SuccessBottomBar(
                onViewBookings = {
                    navController.navigate(Routes.MY_BOOKINGS) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onBackHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Background Gradient (Top)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp) // Chiều cao gradient
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AccentMint.copy(alpha = 0.5f),
                                AccentMint.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // 2. Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Success Icon Animation
                SuccessIcon()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Booking Confirmed!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyDeep,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your court has been successfully reserved.\nYou are all set to play!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NavyDeep.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Ticket Card
                TicketCard()
            }
        }
    }
}

@Composable
fun SuccessIcon() {
    // Giả lập hiệu ứng glow bằng Box xếp chồng
    Box(contentAlignment = Alignment.Center) {
        // Outer Glow (Gradient Radial)
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryGreen.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )

        // White Circle Ring
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, SurfaceCardColor, CircleShape)
                .shadow(8.dp, CircleShape, spotColor = PrimaryGreen.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            // Green Circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(PrimaryGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun TicketCard() {
    // HTML: bg-cool-gray rounded-lg p-6 shadow-sm border
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardColor.copy(alpha = 0.5f)), // Nhạt hơn chút cho đẹp
        border = BorderStroke(1.dp, Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box {
            // Decorative Blur Circle (Top Right)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        Brush.radialGradient(colors = listOf(AccentMint, Color.Transparent))
                    )
            )

            Column(modifier = Modifier.padding(24.dp)) {
                // Venue Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("VENUE", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Skyline Pickleball Center", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = NavyDeep.copy(0.7f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Court 4 (Outdoor)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = NavyDeep.copy(0.7f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = NavyDeep.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(20.dp))

                // Date & Time Grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Date
                    Column(modifier = Modifier.weight(1f)) {
                        Text("DATE", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, null, tint = NavyDeep, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Nov 14, 2023", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }

                    // Time
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TIME", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, null, tint = NavyDeep, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("14:00 - 15:30", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessBottomBar(
    onViewBookings: () -> Unit,
    onBackHome: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, SurfaceCardColor)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onViewBookings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("View My Bookings", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
            }

            OutlinedButton(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, SurfaceCardColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyDeep)
            ) {
                Text("Back to Home", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

