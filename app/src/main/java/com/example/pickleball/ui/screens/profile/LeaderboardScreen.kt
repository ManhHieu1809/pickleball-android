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
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.theme.*

// --- Data Models ---
data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val title: String,
    val elo: Int,
    val imageUrl: String,
    val trend: Int = 0 // 0: none, 1: up
)

// --- Dummy Data (Giống hệt ảnh) ---
val podiumUsers = listOf(
    LeaderboardUser(2, "Davide R.", "", 2810, "https://i.pravatar.cc/150?u=2"),
    LeaderboardUser(1, "Sarah J.", "", 2850, "https://i.pravatar.cc/150?u=1", trend = 1),
    LeaderboardUser(3, "Marcus T.", "", 2795, "https://i.pravatar.cc/150?u=3")
)

val listUsers = listOf(
    LeaderboardUser(4, "Alex Chen", "Pro Member", 2750, "https://i.pravatar.cc/150?u=4"),
    LeaderboardUser(5, "Jessica Wu", "Rising Star", 2720, "https://i.pravatar.cc/150?u=5"),
    LeaderboardUser(6, "Mike Ross", "Veteran", 2695, "https://i.pravatar.cc/150?u=6"),
    LeaderboardUser(7, "Linda K.", "Pro Member", 2640, "https://i.pravatar.cc/150?u=7"),
    LeaderboardUser(8, "Tom Hardy", "Newcomer", 2610, "https://i.pravatar.cc/150?u=8"),
    LeaderboardUser(9, "Sam Smith", "Member", 2580, "https://i.pravatar.cc/150?u=9"),
    LeaderboardUser(10, "John Doe", "Member", 2550, "https://i.pravatar.cc/150?u=10")
)

val currentUser = LeaderboardUser(42, "You", "Top 15%", 1850, "https://i.pravatar.cc/150?u=99", trend = 1)

@Composable
fun LeaderboardScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = { LeaderboardTopBar(onBackClick) },
        bottomBar = { CurrentUserRankBar(user = currentUser) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp) // Padding để không bị che bởi bottom bar
        ) {
            // 1. Podium (Top 3)
            item {
                PodiumSection(podiumUsers)
            }

            // 2. Header List
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("RANKING", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 1.sp)
                    Text("ELO RATING", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 1.sp)
                }
            }

            // 3. List Items
            items(listUsers) { user ->
                RankListItem(user)
            }
        }
    }
}

// --- Components ---

@Composable
fun LeaderboardTopBar(onBackClick: () -> Unit) {
    var selectedFilter by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        // Header Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .background(CoolGray.copy(0.3f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
            }

            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Segmented Control (Toggle Switch)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(48.dp)
                .background(CoolGray, RoundedCornerShape(12.dp)) // Màu nền xám nhạt
                .padding(4.dp)
        ) {
            FilterTab("Global", selectedFilter == 0) { selectedFilter = 0 }
            FilterTab("Friends", selectedFilter == 1) { selectedFilter = 1 }
            FilterTab("Local", selectedFilter == 2) { selectedFilter = 2 }
        }
    }
}

@Composable
fun RowScope.FilterTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PrimaryGreen else Color.Transparent
    val textColor = if (isSelected) NavyDeep else NavyDeep.copy(0.5f)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(bgColor, RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

@Composable
fun PodiumSection(users: List<LeaderboardUser>) {
    val first = users.find { it.rank == 1 } ?: return
    val second = users.find { it.rank == 2 } ?: return
    val third = users.find { it.rank == 3 } ?: return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Rank 2 (Left)
            PodiumItem(user = second, scale = 0.85f, isFirst = false)

            Spacer(modifier = Modifier.width(16.dp))

            // Rank 1 (Center - Higher & Glowing)
            PodiumItem(user = first, scale = 1.1f, isFirst = true)

            Spacer(modifier = Modifier.width(16.dp))

            // Rank 3 (Right)
            PodiumItem(user = third, scale = 0.85f, isFirst = false)
        }
    }
}

@Composable
fun PodiumItem(user: LeaderboardUser, scale: Float, isFirst: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = if (isFirst) (-20).dp else 0.dp) // Đẩy hạng 1 lên cao hơn
    ) {

        // Avatar Box
        Box(contentAlignment = Alignment.BottomCenter) {
            // Glow Effect for #1
            if (isFirst) {
                Box(
                    modifier = Modifier
                        .size((100 * scale).dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(PrimaryGreen.copy(0.6f), Color.Transparent)
                            )
                        )
                        .blur(radius = 20.dp)
                )
            }

            // Avatar Image
            AsyncImage(
                model = user.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size((80 * scale).dp)
                    .clip(CircleShape)
                    .border(
                        width = if (isFirst) 4.dp else 4.dp,
                        color = if (isFirst) PrimaryGreen else Color.White,
                        shape = CircleShape
                    )
                    .shadow(if (isFirst) 10.dp else 4.dp, CircleShape)
            )

            // ELO Badge (Overlapping bottom)
            Surface(
                color = SoftMint,
                shape = RoundedCornerShape(50),
                border = BorderStroke(2.dp, Color.White),
                shadowElevation = 2.dp,
                modifier = Modifier.offset(y = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    if (user.trend > 0) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = NavyDeep
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    Text(
                        text = "${user.elo} ELO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NavyDeep
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${user.rank}",
                fontWeight = FontWeight.Black,
                color = if (user.rank == 1) Color(0xFFFFD700) else NavyDeep,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
fun RankListItem(user: LeaderboardUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray.copy(alpha = 0.5f)), // Màu xám nhạt như ảnh
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Number
            Text(
                text = "${user.rank}",
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                fontSize = 16.sp,
                modifier = Modifier.width(28.dp)
            )

            // Avatar
            AsyncImage(
                model = user.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Name & Title
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep,
                    fontSize = 14.sp
                )
                Text(
                    text = user.title,
                    fontWeight = FontWeight.Medium,
                    color = NavyDeep.copy(0.5f),
                    fontSize = 11.sp
                )
            }

            // ELO Score
            Text(
                text = "${user.elo}",
                fontWeight = FontWeight.Black,
                color = NavyDeep,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CurrentUserRankBar(user: LeaderboardUser) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 20.dp,
        border = BorderStroke(1.dp, CoolGray.copy(0.5f)),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            // Background Container styling
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoolGray.copy(0.5f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Green Vertical Indicator Line
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(36.dp)
                        .background(PrimaryGreen, RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Rank
                Text(
                    text = "${user.rank}",
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep,
                    fontSize = 18.sp,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )

                // Avatar
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Info (Name & Trend)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "You",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.title, // "Top 15%"
                            fontSize = 11.sp,
                            color = NavyDeep.copy(0.6f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Highlighted ELO Box (Green)
                Surface(
                    color = PrimaryGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${user.elo}",
                        fontWeight = FontWeight.Black,
                        color = NavyDeep,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
