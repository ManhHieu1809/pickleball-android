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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*

val NavyDarker = Color(0xFF020617)

@Composable
fun PreMatchLobbyScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LobbyAnim")

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -10f, targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "Float"
    )

    val spinRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Restart),
        label = "Spin"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "Pulse"
    )

    var selectedRadius by remember { mutableIntStateOf(10) }

    Scaffold(containerColor = NavyDarker) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LobbyBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LobbyHeader(onBackClick)

                Spacer(modifier = Modifier.weight(0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset(y = floatOffset.dp)
                    ) {
                        RankDiamond(spinRotation)
                        Spacer(modifier = Modifier.height(16.dp))
                        RankTitle()
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    InviteDuoButton(modifier = Modifier.offset(y = 30.dp), navController = navController)
                }

                Spacer(modifier = Modifier.height(32.dp))

                LobbyStatsRow(pulseScale)

                Spacer(modifier = Modifier.height(24.dp))

                LobbyRadiusSelector(
                    selectedRadius = selectedRadius,
                    onRadiusSelected = { selectedRadius = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                LobbyCareerHighs()

                Spacer(modifier = Modifier.height(32.dp))

                StartButton { navController.navigate(Routes.SEARCHING_MATCH) }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun LobbyBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-100).dp)
                .size(400.dp, 300.dp)
                .background(Brush.radialGradient(colors = listOf(PrimaryGreen.copy(0.25f), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .size(300.dp)
                .background(Brush.radialGradient(colors = listOf(NavyDeep.copy(0.8f), Color.Transparent)))
        )
    }
}

@Composable
private fun LobbyHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = Color.White.copy(0.05f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.White.copy(0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(PrimaryGreen, CircleShape)
                            .shadow(4.dp, CircleShape, spotColor = PrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "ONLINE",
                        fontFamily = Lexend,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White.copy(0.1f), CircleShape)
                    .border(1.dp, Color.White.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun RankDiamond(spinRotation: Float) {
    val size = 160.dp

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size * 1.4f).rotate(spinRotation)) {
            drawCircle(
                brush = Brush.sweepGradient(colors = listOf(Color.Transparent, PrimaryGreen.copy(0.4f), Color.Transparent)),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                )
            )
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .rotate(45f)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color.White.copy(0.1f), NavyDarker.copy(0.5f)),
                        start = Offset(0f, 0f),
                        end = Offset(200f, 200f)
                    ),
                    RoundedCornerShape(24.dp)
                )
                .border(
                    1.dp,
                    Brush.linearGradient(colors = listOf(PrimaryGreen.copy(0.8f), Color.Transparent)),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize().alpha(0.1f)) {
                drawRoundRect(
                    color = Color.White,
                    cornerRadius = CornerRadius(24.dp.toPx()),
                    style = Stroke(width = 1f)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Diamond,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(60.dp)

        )
    }
}

@Composable
private fun RankTitle() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "PLATINUM IV",
            fontFamily = Lexend,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = Color.White,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(3.dp)
                .background(Brush.horizontalGradient(colors = listOf(Color.Transparent, PrimaryGreen, Color.Transparent)))
                .shadow(8.dp, CircleShape, spotColor = PrimaryGreen)
        )
    }
}

@Composable
private fun InviteDuoButton(modifier: Modifier = Modifier, navController: NavController) {
    val boxSize = 112.dp

    Box(
        modifier = modifier
            .size(boxSize)
            .clickable { navController.navigate(Routes.INVITE_DUO) },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(80.dp).rotate(45f)) {
            drawRoundRect(
                color = Color.White.copy(0.15f),
                size = size,
                cornerRadius = CornerRadius(16.dp.toPx()),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )
            drawRoundRect(
                color = NavyDeep.copy(0.4f),
                size = size,
                cornerRadius = CornerRadius(16.dp.toPx())
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(PrimaryGreen.copy(0.1f), CircleShape)
                    .border(1.dp, PrimaryGreen, CircleShape)
                    .shadow(10.dp, CircleShape, spotColor = PrimaryGreen.copy(0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "INVITE DUO",
                fontFamily = Lexend,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen.copy(0.9f),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun LobbyStatsRow(pulseScale: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("ELO SCORE", fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.5f), letterSpacing = 2.sp)
            Text("1,850", fontFamily = Lexend, fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.width(32.dp))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(32.dp)
                .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.White.copy(0.2f), Color.Transparent)))
        )
        Spacer(modifier = Modifier.width(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("WIN STREAK", fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.5f), letterSpacing = 2.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocalFireDepartment,
                    null,
                    tint = PrimaryGreen,
                    modifier = Modifier.scale(pulseScale).size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("5", fontFamily = Lexend, fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

@Composable
private fun LobbyRadiusSelector(selectedRadius: Int, onRadiusSelected: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MATCHMAKING RADIUS", fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.6f), letterSpacing = 1.sp)
            Text("$selectedRadius km", fontFamily = Lexend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyDeep.copy(0.4f), RoundedCornerShape(8.dp))
                .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadiusOptionButton(5, selectedRadius == 5) { onRadiusSelected(5) }
            RadiusOptionButton(10, selectedRadius == 10) { onRadiusSelected(10) }
            RadiusOptionButton(20, selectedRadius == 20) { onRadiusSelected(20) }
        }
    }
}

@Composable
private fun RowScope.RadiusOptionButton(value: Int, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PrimaryGreen else Color.Transparent
    val textColor = if (isSelected) NavyDeep else Color.White.copy(0.5f)

    Box(
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .clickable { onClick() }
            .then(if(isSelected) Modifier.shadow(10.dp, RoundedCornerShape(6.dp), spotColor = PrimaryGreen.copy(0.5f)) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$value km",
            fontFamily = Lexend,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun LobbyCareerHighs() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(0.1f))
            Text(
                "CAREER HIGHS",
                modifier = Modifier.padding(horizontal = 12.dp),
                fontFamily = Lexend,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(0.5f),
                letterSpacing = 2.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(0.1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CareerStatItem("TOTAL MVPS", "42", Icons.Default.MilitaryTech)
            CareerStatItem("BEST RANK", "DIA I", Icons.Default.WorkspacePremium)
            CareerStatItem("TOURNEYS", "12", Icons.Default.EmojiEvents)
        }
    }
}

@Composable
private fun RowScope.CareerStatItem(label: String, value: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(NavyDeep.copy(0.5f), RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(0.15f), RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = PrimaryGreen.copy(0.8f), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, fontFamily = Lexend, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(label, fontFamily = Lexend, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.5f), letterSpacing = 1.sp)
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(16.dp, RoundedCornerShape(12.dp), spotColor = PrimaryGreen.copy(0.4f)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "START MATCHMAKING",
                fontFamily = Lexend,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp))
        }
    }
}