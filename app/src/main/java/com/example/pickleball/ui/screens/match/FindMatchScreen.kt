package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*

val SoftMint = Color(0xFFD6FFF3)
val CoolGray = Color(0xFFE8EBF0)

@Composable
fun FindMatchScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onMatchClick: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = { FindMatchTopBar(onBackClick) },
        bottomBar = {
            BottomNav(
                selectedTab = HomeTab.MATCHES,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.HOME -> navController.navigateToTab(Routes.HOME)
                        HomeTab.MATCHES -> { /* Đang ở đây */ }
                        HomeTab.COURTS -> navController.navigateToTab(Routes.FIND_COURT)
                        HomeTab.BOOKINGS -> navController.navigateToTab(Routes.MY_BOOKINGS)
                        HomeTab.PROFILE -> navController.navigateToTab(Routes.PROFILE)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.CREATE_MATCH)
                },
                containerColor = PrimaryNeon,
                contentColor = NavyDeep,
                shape = CircleShape,
                modifier = Modifier
                    .size(64.dp)
                    .shadow(16.dp, spotColor = PrimaryNeon, shape = CircleShape)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Create Match",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SoftMint.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { SegmentedFilter() }
                item { HorizontalChipList() }
                items(sampleMatches) { match ->
                    MatchCard(
                        match = match,
                        onClick = { onMatchClick("dummy_match_id_123") }
                    )
                }
            }
        }
    }
}

data class MatchData(
    val day: String,
    val date: String,
    val title: String,
    val location: String,
    val court: String,
    val time: String,
    val type: String,
    val needsText: String,
    val eloText: String,
    val players: List<String>,
    val isFull: Boolean = false
)

val sampleMatches = listOf(
    MatchData("NOV", "14", "Doubles Scramble", "Skyline Center", "Court 4", "14:00 PM", "Casual", "Needs 1 more", "Avg ELO 3.5", listOf("https://i.pravatar.cc/150?img=1", "https://i.pravatar.cc/150?img=2"), false),
    MatchData("NOV", "14", "Advanced Singles", "City Park Courts", "Court 1", "16:30 PM", "Ranked", "Needs 1 more", "Avg ELO 4.5", listOf("https://i.pravatar.cc/150?img=3"), false),
    MatchData("NOV", "15", "Friday Morning Mix", "The Kitchen", "Court 8", "08:00 AM", "Casual", "Full Squad", "Avg ELO 3.2", listOf("https://i.pravatar.cc/150?img=4", "https://i.pravatar.cc/150?img=5", "https://i.pravatar.cc/150?img=6", "https://i.pravatar.cc/150?img=7"), true),
    MatchData("NOV", "15", "Pro Practice", "Elite Sports Club", "Court 1", "18:00 PM", "Ranked", "Needs 2 more", "Avg ELO 5.0+", listOf("https://i.pravatar.cc/150?img=8", "https://i.pravatar.cc/150?img=9"), false),
)

@Composable
fun FindMatchTopBar(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.9f))
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
            }
            Text(
                text = "Find a Match",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDeep
            )
            Box {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = "Noti", tint = NavyDeep)
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(8.dp)
                        .background(PrimaryGreen, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(CoolGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .border(1.dp, CoolGray, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = NavyDeep.copy(0.4f))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    textStyle = TextStyle(color = NavyDeep, fontSize = 14.sp, fontWeight = FontWeight.Medium),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (true) Text("Search matches, courts, or players...", color = NavyDeep.copy(0.4f), fontSize = 14.sp)
                        innerTextField()
                    }
                )
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Tune, null, tint = NavyDeep.copy(0.6f))
            }
        }
        HorizontalDivider(color = CoolGray.copy(alpha = 0.5f), modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun SegmentedFilter() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f).height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = NavyDeep),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Casual", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
        TextButton(
            onClick = {},
            modifier = Modifier.weight(1f).height(36.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Ranked", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.6f))
        }
        TextButton(
            onClick = {},
            modifier = Modifier.weight(1f).height(36.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Tournament", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.6f))
        }
    }
}

@Composable
fun HorizontalChipList() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { ChipItem("Nearby", Icons.Default.LocationOn) }
        item { ChipItem("Today", Icons.Default.CalendarToday) }
        item { ChipItem("ELO 3.0+", Icons.Default.Bolt) }
        item { ChipItem("Map View", Icons.Default.Map) }
    }
}

@Composable
fun ChipItem(text: String, icon: ImageVector) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, CoolGray),
        color = Color.White,
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = NavyDeep)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep)
        }
    }
}

@Composable
fun MatchCard(
    match: MatchData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            val contentAlpha = if (match.isFull) 0.5f else 1f

            Column(modifier = Modifier.padding(16.dp).alpha(contentAlpha)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(0.5f), RoundedCornerShape(10.dp))
                                .padding(vertical = 6.dp)
                        ) {
                            Text(match.day, style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, color = NavyDeep.copy(0.4f), fontWeight = FontWeight.Bold)
                            Text(match.date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = NavyDeep)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(match.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = NavyDeep.copy(0.6f))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${match.location} • ${match.court}", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = NavyDeep.copy(0.6f))
                            }
                        }
                    }

                    // Badge & Time
                    Column(horizontalAlignment = Alignment.End) {
                        val badgeColor = if(match.type == "Casual") SoftMint else NavyDeep
                        val badgeTextColor = if(match.type == "Casual") NavyDeep else Color.White

                        Surface(
                            color = badgeColor,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = match.type.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(match.time, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = NavyDeep.copy(0.05f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Players Avatars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.CenterStart) {
                            match.players.forEachIndexed { index, url ->
                                AsyncImage(
                                    model = url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(start = (index * 24).dp) // Overlap effect
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, CoolGray, CircleShape)
                                        .zIndex(match.players.size - index.toFloat())
                                )
                            }
                            // "+1" Bubble if needed (Example logic)
                            if (match.players.size < 2 && !match.isFull) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = (match.players.size * 24).dp)
                                        .size(32.dp)
                                        .background(Color.White, CircleShape)
                                        .border(2.dp, CoolGray, CircleShape)
                                        .zIndex(0f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+1", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(match.needsText, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = NavyDeep.copy(0.5f))
                            Text(match.eloText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }

                    // Action Button
                    Button(
                        onClick = {},
                        enabled = !match.isFull,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (match.isFull) Color.White else PrimaryGreen,
                            contentColor = if (match.isFull) NavyDeep.copy(0.4f) else NavyDeep,
                            disabledContainerColor = Color.White,
                            disabledContentColor = NavyDeep.copy(0.4f)
                        ),
                        border = if(match.isFull) BorderStroke(1.dp, CoolGray) else null,
                        elevation = if(match.isFull) ButtonDefaults.buttonElevation(0.dp) else ButtonDefaults.buttonElevation(2.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(if (match.isFull) "Full" else "Join Match", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Full Overlay Badge (Centered)
            if (match.isFull) {
                Box(
                    modifier = Modifier.matchParentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = NavyDeep,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "Full",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}