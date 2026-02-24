package com.example.pickleball.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.theme.*

val DefeatRed = Color(0xFFEF4444)
val DefeatTextGray = Color(0xFF94A3B8)
val CardBackground = Color(0xFFF1F5F9)

data class MatchHistoryItem(
    val type: String,
    val date: String,
    val result: MatchResultType,
    val opponent: String,
    val score: String,
    val setInfo: String,
    val location: String,
    val eloChange: Int? = null
)

enum class MatchResultType { VICTORY, DEFEAT }

// --- Dummy Data ---
val matchHistoryData = listOf(
    MatchHistoryItem("RANKED", "Today, 10:30 AM", MatchResultType.VICTORY, "Marcus T.", "11-9, 11-8", "STRAIGHT SETS", "Sunset Cliffs, Court 1", 24),
    MatchHistoryItem("RANKED", "Yesterday", MatchResultType.DEFEAT, "Sarah J.", "9-11, 11-4, 8-11", "3 SETS", "Downtown Rec, Court 4", -15),
    MatchHistoryItem("CASUAL", "Oct 20", MatchResultType.VICTORY, "Alex Chen", "11-5, 11-2", "STRAIGHT SETS", "Private Club, Main Court", null),
    MatchHistoryItem("RANKED", "Oct 18", MatchResultType.VICTORY, "Mike Ross", "13-11, 11-9", "STRAIGHT SETS", "City Center Courts, Court 2", 18)
)

@Composable
fun MatchHistoryScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            MatchHistoryTopBar(onBackClick)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Recent Form Card
            item { RecentFormCard() }

            // 2. List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Recent Matches", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = CoolGray, shape = RoundedCornerShape(6.dp)) {
                            Text("12", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }

                    Surface(
                        color = NavyDeep.copy(0.05f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.clickable { /* Filter Action */ }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("FILTER", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.FilterList, null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // 3. Match List
            items(matchHistoryData) { match ->
                MatchHistoryCard(
                    match = match,
                    onClick = { navController.navigate(Routes.MATCH_ANALYSIS) }
                )
            }
        }
    }
}

// --- Components ---

@Composable
fun MatchHistoryTopBar(onBackClick: () -> Unit) {
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
                .size(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "Match History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun RecentFormCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = NavyDeep.copy(0.2f))
            .clip(RoundedCornerShape(16.dp))
            .background(NavyDeep)
    ) {
        // Decorative Glows
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(PrimaryGreen.copy(0.1f), CircleShape)
        )

        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recent Form (W/L)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("RECENT FORM", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.History, null, tint = Color.White.copy(0.6f), modifier = Modifier.size(14.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FormBadge(isWin = true)
                    FormBadge(isWin = true)
                    FormBadge(isWin = false)
                    FormBadge(isWin = true)
                    FormBadge(isWin = false)
                }
            }

            // Vertical Divider
            Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.White.copy(0.1f)))

            // Avg ELO Gain
            Column(horizontalAlignment = Alignment.End) {
                Text("AVG ELO GAIN", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("+14.2", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = PrimaryGreen)
                }
            }
        }
    }
}

@Composable
fun FormBadge(isWin: Boolean) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                if (isWin) PrimaryGreen else Color.White.copy(0.05f),
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                if (isWin) PrimaryGreen else Color.White.copy(0.1f),
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isWin) "W" else "L",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = if (isWin) NavyDeep else Color.White.copy(0.5f)
        )
    }
}

@Composable
fun MatchHistoryCard(
    match: MatchHistoryItem,
    onClick: () -> Unit
) {
    val accentColor = when (match.result) {
        MatchResultType.VICTORY -> PrimaryGreen
        MatchResultType.DEFEAT -> DefeatRed
    }

    // Victory text is Green, Defeat text is Grey (as per image design)
    val titleText = if (match.result == MatchResultType.VICTORY) "VICTORY" else "DEFEAT"
    val titleColor = if (match.result == MatchResultType.VICTORY) PrimaryGreen else DefeatTextGray.copy(alpha = 0.8f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground), // Nền xám nhạt #F1F5F9
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Row chứa dải màu và nội dung
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // 1. Dải màu dọc bên trái (Accent Strip)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            // 2. Nội dung chính
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Header: Badge + Date + ELO Change
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if(match.type == "RANKED") NavyDeep else Color.White,
                            shape = RoundedCornerShape(4.dp),
                            border = if(match.type == "CASUAL") BorderStroke(1.dp, NavyDeep.copy(0.1f)) else null
                        ) {
                            Text(
                                match.type,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if(match.type == "RANKED") PrimaryGreen else NavyDeep.copy(0.7f),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(match.date, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.5f))
                    }

                    if (match.eloChange != null) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color.Black.copy(0.05f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (match.eloChange > 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = if (match.eloChange > 0) PrimaryGreen else DefeatRed,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = if (match.eloChange > 0) "+${match.eloChange}" else "${match.eloChange}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (match.eloChange > 0) PrimaryGreen else DefeatRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Content: Result vs Opponent & Score
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Result & Opponent
                    Column {
                        Text(
                            text = titleText,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            color = titleColor,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("vs.", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.5f))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(match.opponent, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }

                    // Score & Set Info
                    Column(horizontalAlignment = Alignment.End) {
                        Text(match.score, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                        Text(match.setInfo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 0.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = NavyDeep.copy(0.05f))
                Spacer(modifier = Modifier.height(12.dp))

                // Footer Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Stadium, null, tint = NavyDeep.copy(0.4f), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(match.location, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.5f))
                }
            }
        }
    }
}