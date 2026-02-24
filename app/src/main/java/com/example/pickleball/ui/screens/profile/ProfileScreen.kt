package com.example.pickleball.ui.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*


val CardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF050A30), Color(0xFF0A1450))
)
val GoldColor = Color(0xFFFFD700)

@Composable
fun ProfileScreen(
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { ProfileTopBar(onBackClick) },
        bottomBar = {
            BottomNav(
                selectedTab = HomeTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.HOME -> navController.navigateToTab(Routes.HOME)
                        HomeTab.MATCHES -> navController.navigateToTab(Routes.FIND_MATCH)
                        HomeTab.COURTS -> navController.navigateToTab(Routes.FIND_COURT)
                        HomeTab.BOOKINGS -> navController.navigateToTab(Routes.MY_BOOKINGS)
                        HomeTab.PROFILE -> { /* Đang ở đây */ }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Mesh Gradient
            MeshBackground()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Main Profile Card
                item { MainProfileCard(
                    onLeaderboardClick = {
                        navController.navigate(Routes.LEADERBOARD)
                    }
                ) }

                item { QuickActionsRow(navController) }
                item { SeasonStatsSection() }
                item { EquippedTitlesSection() }
                item { PreferencesSection() }
            }
        }
    }
}

@Composable
fun ProfileTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "Player Profile",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NavyDeep
        )

        IconButton(
            onClick = { /* Settings */ },
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = NavyDeep)
        }
    }
}

@Composable
fun MainProfileCard(
    onLeaderboardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = NavyDeep.copy(0.3f))
            .clip(RoundedCornerShape(24.dp))
            .background(CardGradient)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 100.dp, y = (-80).dp)
                .align(Alignment.TopEnd)
                .background(PrimaryGreen.copy(0.15f), CircleShape)
                .blur(radius = 50.dp)
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("MEMBER ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.4f), letterSpacing = 1.sp)
                    Text("PKL-883920", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen, letterSpacing = 1.sp)
                }

                Surface(
                    color = Color.White.copy(0.1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.White.copy(0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(PrimaryGreen, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PRO MEMBER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(PrimaryGreen.copy(0.2f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Brush.verticalGradient(listOf(PrimaryGreen, SoftMint, Color.Transparent)), CircleShape)
                            .padding(3.dp)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBjq88HLWVFED9C6GjSps3jYZX4nDTmPbRspIbca2VY1lTekGmdDYZqyIweozI7wtMPc3WfCux0CUy2gFjxT_yUg_sGRfSy0jK1kJ4128TNBUmsiWUn8d-ENIdhvx4ECa5DYyPzdzr30jMnnjpRaxxizaPzPTqlpSgLQIN5F50_qZqwqccQHBbFBxLV_u_pFCge89YtNeOQ8t7irn-3EHy0RMHN5zX5SzkjX-OSpsDf8Tt2jlXb1VxFjpTKgvMtRN4Sc8WGF4EO-OtM", // Avatar URL
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, NavyDeep, CircleShape)
                                .background(NavyDeep),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-4).dp, y = (-4).dp)
                            .background(NavyDeep, CircleShape)
                            .border(2.dp, NavyDeep, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Marcus Chen", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(0.6f), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("LA, California", fontSize = 12.sp, color = Color.White.copy(0.6f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLeaderboardClick()
                    },
                color = NavyDeep.copy(0.5f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.White.copy(0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Leaderboard, null, tint = PrimaryGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("GLOBAL RANKING", fontSize = 10.sp, color = Color.White.copy(0.5f), fontWeight = FontWeight.Bold)
                            Text("#42 Top Contender", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White.copy(0.5f))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.White.copy(0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("MEMBER SINCE", fontSize = 10.sp, color = Color.White.copy(0.4f), fontWeight = FontWeight.Bold)
                    Text("SEP 2023", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Text(
                    "EDIT PROFILE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clickable { }
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun QuickActionsRow(navController: NavController) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickActionItem(
            label = "Wallet",
            icon = Icons.Outlined.AccountBalanceWallet,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.WALLET) }
        )
        QuickActionItem(
            label = "Bookings",
            icon = Icons.Outlined.CalendarMonth,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.MY_BOOKINGS) }
        )
        QuickActionItem(
            label = "Matches",
            icon = Icons.Outlined.History,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.MATCH_HISTORY) }
        )
    }
}

@Composable
fun QuickActionItem(label: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit = {}) {
    Surface(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CoolGray),
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SoftMint.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = NavyDeep, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
        }
    }
}

@Composable
fun SeasonStatsSection() {
    Column {
        Text("SEASON STATISTICS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatBox(
                icon = Icons.Default.SportsTennis,
                value = "82",
                label = "MATCHES",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                icon = Icons.Default.Percent,
                value = "68%",
                label = "WIN RATE",
                modifier = Modifier.weight(1f),
                showGraph = true
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatBox(
                icon = Icons.Default.EmojiEvents,
                value = "56",
                label = "WINS",
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.BottomStart
            )
            StatBox(
                icon = Icons.Default.SentimentDissatisfied,
                value = "26",
                label = "LOSSES",
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.BottomStart
            )
        }
    }
}

@Composable
fun StatBox(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier,
    showGraph: Boolean = false,
    contentAlign: Alignment = Alignment.TopStart
) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = CoolGray,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Icon(
                icon, null,
                tint = NavyDeep.copy(0.3f),
                modifier = Modifier.align(Alignment.TopStart).size(24.dp)
            )

            Column(modifier = Modifier.align(if (contentAlign == Alignment.BottomStart) Alignment.BottomStart else Alignment.CenterStart).padding(top = 24.dp)) {
                Text(value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = NavyDeep)
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.5f))
            }

            if (showGraph) {
                Icon(
                    Icons.Default.TrendingUp, null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.TopEnd).size(40.dp).alpha(0.5f)
                )
            }
        }
    }
}

@Composable
fun EquippedTitlesSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("EQUIPPED TITLES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 1.sp)
            Text("VIEW ALL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                TitleItem(
                    name = "Champion",
                    icon = Icons.Default.EmojiEvents,
                    isActive = true,
                    color = PrimaryGreen
                )
            }
            item {
                TitleItem(
                    name = "Hot Streak",
                    icon = Icons.Default.LocalFireDepartment,
                    isActive = false,
                    color = NavyDeep
                )
            }
            item {
                TitleItem(
                    name = "Power Serve",
                    icon = Icons.Default.Bolt,
                    isActive = false,
                    color = NavyDeep
                )
            }
            item {
                TitleItem(
                    name = "Locked",
                    icon = Icons.Default.Lock,
                    isActive = false,
                    color = NavyDeep.copy(0.3f),
                    isLocked = true
                )
            }
        }
    }
}

@Composable
fun TitleItem(name: String, icon: ImageVector, isActive: Boolean, color: Color, isLocked: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(72.dp)) {
        Box(
            modifier = Modifier.size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect for active
            if (isActive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrimaryGreen.copy(0.2f), CircleShape)
                        .blur(10.dp)
                )
            }

            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (isActive) NavyDeep else Color.White,
                border = BorderStroke(1.dp, if (isActive) PrimaryGreen else CoolGray),
                shadowElevation = if (isActive) 8.dp else 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = if (isActive) PrimaryGreen else color, modifier = Modifier.size(28.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            name.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) PrimaryGreen else color,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun PreferencesSection() {
    Column {
        Text("PREFERENCES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, CoolGray),
            shadowElevation = 2.dp
        ) {
            Column {
                PreferenceItem("Account Settings", Icons.Outlined.Tune)
                Divider(color = CoolGray)
                PreferenceItem("Notifications", Icons.Outlined.Notifications)
                Divider(color = CoolGray)
                PreferenceItem("Privacy & Security", Icons.Outlined.Security)
            }
        }
    }
}

@Composable
fun PreferenceItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(CoolGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = NavyDeep, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = NavyDeep.copy(0.3f))
    }
}
fun Modifier.blur(radius: androidx.compose.ui.unit.Dp) = this.then(
    Modifier.drawBehind {
        drawRect(Color.Transparent)
    }
)

@Composable
fun MeshBackground() {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(SoftMint.copy(0.4f), Color.Transparent),
                        center = Offset(1000f, 0f),
                        radius = 800f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryGreen.copy(0.1f), Color.Transparent),
                        center = Offset(0f, 1500f),
                        radius = 800f
                    )
                )
        )
    }
}