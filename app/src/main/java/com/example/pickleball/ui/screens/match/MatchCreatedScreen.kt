package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*

// --- COLORS ---
// Dùng lại màu đã có
// val PrimaryGreen = Color(0xFF00F684)
// val NavyDark = Color(0xFF050A30)
// val CoolGrayLight = Color(0xFFE8EBF0)
// val SoftMintLight = Color(0xFFD6FFF3)
// val WhitePure = Color(0xFFFFFFFF)

@Composable
fun MatchCreatedScreen(
    navController: NavController,
    onViewMyMatches: () -> Unit,
    onShare: () -> Unit,
    onBackToHome: () -> Unit
) {
    Scaffold(
        containerColor = WhitePure
    ) { paddingValues ->
        // Background Mesh Gradient
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SoftMintLight.copy(0.4f), Color.Transparent),
                            radius = 800f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 1. Success Header
                SuccessHeaderCasual()

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Match Details Card
                MatchDetailsCard()

                Spacer(modifier = Modifier.weight(1f)) // Push buttons to bottom

                // 3. Action Buttons
                ActionButtons(
                    onViewMyMatches = onViewMyMatches,
                    onShare = onShare,
                    onBackToHome = onBackToHome
                )
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun SuccessHeaderCasual() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(SoftMintLight, CircleShape)
                .border(4.dp, Color.Transparent, CircleShape) // Placeholder for pulse anim if needed
                .shadow(15.dp, CircleShape, spotColor = PrimaryGreen.copy(0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = PrimaryGreen,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Match Created!",
            fontFamily = Lexend,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = NavyDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your court is booked and ready.",
            fontFamily = Lexend,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = NavyDark.copy(0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MatchDetailsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WhitePure),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, CoolGrayLight)
    ) {
        Column {
            // Green Top Bar
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(PrimaryGreen))

            Column(modifier = Modifier.padding(24.dp)) {
                // Header: Type & ELO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("MATCH TYPE", fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark.copy(0.4f))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SportsTennis, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Doubles - Ranked", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NavyDark)
                        }
                    }

                    Surface(color = CoolGrayLight, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            "ELO 1200+",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark,
                            fontFamily = Lexend,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = CoolGrayLight)
                Spacer(modifier = Modifier.height(24.dp))

                // Location
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "LOCATION",
                    title = "Venice Beach Courts",
                    subtitle = "1800 Ocean Front Walk • Court 4"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Date & Time
                DetailRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "DATE & TIME",
                    title = "Today, Oct 28",
                    subtitle = "10:00 AM - 11:30 AM"
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CoolGrayLight, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = NavyDark, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(label, fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark.copy(0.4f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontFamily = Lexend, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyDark)
            Text(subtitle, fontFamily = Lexend, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = NavyDark.copy(0.6f))
        }
    }
}

@Composable
fun ActionButtons(
    onViewMyMatches: () -> Unit,
    onShare: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // View My Matches (Primary)
        Button(
            onClick = onViewMyMatches,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(10.dp, spotColor = PrimaryGreen.copy(0.3f), shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDark)
        ) {
            Icon(Icons.Default.Visibility, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("VIEW MY MATCHES", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, letterSpacing = 1.sp)
        }

        // Share (Secondary)
        OutlinedButton(
            onClick = onShare,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, CoolGrayLight),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = WhitePure, contentColor = NavyDark)
        ) {
            Icon(Icons.Default.Share, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("SHARE MATCH LINK", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp)
        }

        // Back to Home (Text Link)
        TextButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark.copy(0.5f))
        }
    }
}