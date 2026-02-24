package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.R // Đảm bảo import R để dùng ảnh drawable nếu có
import com.example.pickleball.ui.theme.*

// --- MÀU SẮC TỪ CSS ---
val NavyBg = Color(0xFF050A30)
val PrimaryNeon = Color(0xFF00F684)
val CardGray = Color(0xFFE8EBF0) // #E8EBF0 - Cool Gray
val TextNavy = Color(0xFF050A30)

// --- DATA MODEL GIẢ LẬP ---
data class Friend(
    val name: String,
    val subText: String,
    val isOnline: Boolean,
    val isRecent: Boolean = false,
    val avatarId: Int = android.R.drawable.ic_menu_gallery
)

val onlineFriends = listOf(
    Friend("PickleRick_99", "Platinum II", true),
    Friend("SarahSmash", "Diamond I", true),
    Friend("DinkMasterFlex", "Gold III", true)
)

val recentTeammates = listOf(
    Friend("NetNinja_88", "2h ago", false, isRecent = true),
    Friend("SpinDoctor", "5h ago", false, isRecent = true)
)

@Composable
fun InviteDuoScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = NavyBg,
        bottomBar = {
            // Footer: Back to Lobby
            Column(modifier = Modifier.background(NavyBg)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(0.1f), Color.Transparent)
                            )
                        )
                )
                TextButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White.copy(0.4f))
                ) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "BACK TO LOBBY",
                        fontFamily = Lexend,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 12.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Background Effects
            InviteBackground()

            // 2. Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Header
                InviteHeader()

                Spacer(modifier = Modifier.height(24.dp))

                // Search Bar
                SearchBar()

                Spacer(modifier = Modifier.height(32.dp))

                // Scrollable List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // Section: Online Friends
                    item {
                        SectionHeader("ONLINE FRIENDS", "3 Active", isBadge = true)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items(onlineFriends) { friend ->
                        PlayerCard(friend)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Section: Recent Teammates
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        SectionHeader("RECENT TEAMMATES", "VIEW ALL", isBadge = false)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items(recentTeammates) { friend ->
                        PlayerCard(friend)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// --- SUB COMPONENTS ---

@Composable
fun InviteBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top blob
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-100).dp)
                .size(400.dp, 300.dp)
                .background(Brush.radialGradient(colors = listOf(PrimaryNeon.copy(0.15f), Color.Transparent)))
        )
        // Bottom left blob
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .size(300.dp)
                .background(Brush.radialGradient(colors = listOf(NavyBg.copy(0.8f), Color.Transparent)))
        )
    }
}

@Composable
fun InviteHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Left: Title
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.GroupAdd, null, tint = PrimaryNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "TEAM UP",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Color.White.copy(0.5f),
                    letterSpacing = 2.sp
                )
            }
            Text(
                "INVITE DUO",
                fontFamily = Lexend,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 32.sp,
                color = Color.White,
                letterSpacing = (-1).sp
            )
        }

        // Right: My Rank Badge
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "MY RANK",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                color = Color.White.copy(0.4f),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = Color.White.copy(0.05f),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.White.copy(0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Diamond, null, tint = PrimaryNeon, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PLAT IV", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)

                    // Vertical Divider
                    Box(modifier = Modifier.padding(horizontal = 6.dp).width(1.dp).height(10.dp).background(Color.White.copy(0.2f)))

                    Text("1,850", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = PrimaryNeon)
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(NavyBg.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, null, tint = Color.White.copy(0.5f))
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.weight(1f)) {
            Text(
                "Find friends by name or ID...",
                color = Color.White.copy(0.3f),
                fontSize = 14.sp,
                fontFamily = Lexend,
                fontWeight = FontWeight.Medium
            )
        }
        IconButton(onClick = { /* Scan QR */ }) {
            Icon(Icons.Default.QrCodeScanner, null, tint = Color.White.copy(0.5f))
        }
    }
}

@Composable
fun SectionHeader(title: String, rightText: String, isBadge: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isBadge) {
                Box(modifier = Modifier.size(6.dp).background(PrimaryNeon, CircleShape).shadow(4.dp, CircleShape, spotColor = PrimaryNeon))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                title,
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if(isBadge) Color.White.copy(0.8f) else Color.White.copy(0.6f),
                letterSpacing = 2.sp
            )
        }

        if (isBadge) {
            Surface(
                color = PrimaryNeon.copy(0.1f),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, PrimaryNeon.copy(0.2f))
            ) {
                Text(
                    rightText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontFamily = Lexend,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNeon
                )
            }
        } else {
            Text(
                rightText,
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White.copy(0.4f),
                letterSpacing = 1.sp,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
fun PlayerCard(friend: Friend) {
    val containerColor = if (friend.isRecent) CardGray.copy(0.9f) else CardGray
    // Nếu là Recent thì grayscale nhẹ (giả lập bằng opacity), nếu không thì full color
    val alpha = if (friend.isRecent) 0.9f else 1f

    Card(
        modifier = Modifier.fillMaxWidth().graphicsLayer { this.alpha = alpha },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = NavyBg.copy(0.1f),
                    border = BorderStroke(1.dp, Color.White.copy(0.5f))
                ) {
                    // Placeholder Image
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, tint = TextNavy.copy(0.3f))
                    }
                }
                // Status Indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(12.dp)
                        .background(if (friend.isRecent) Color.Gray else PrimaryNeon, CircleShape)
                        .border(2.dp, CardGray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    friend.name,
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = TextNavy
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (friend.isRecent) Icons.Default.Schedule else Icons.Default.WorkspacePremium,
                        null,
                        tint = TextNavy.copy(0.6f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        friend.subText.uppercase(),
                        fontFamily = Lexend,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = TextNavy.copy(0.7f),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Invite Button
            val btnBg = if(friend.isRecent) Color.White else NavyBg
            val btnText = if(friend.isRecent) TextNavy.copy(0.5f) else Color.White
            val btnBorder = if(friend.isRecent) BorderStroke(1.dp, TextNavy.copy(0.1f)) else null

            Button(
                onClick = { /* Invite Action */ },
                colors = ButtonDefaults.buttonColors(containerColor = btnBg),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                border = btnBorder,
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    "INVITE",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    color = btnText,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}