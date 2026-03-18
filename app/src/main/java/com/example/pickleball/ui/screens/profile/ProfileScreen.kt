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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.navigation.navigateToTab
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.screens.home.components.BottomNav
import com.example.pickleball.ui.screens.home.components.HomeTab
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.ProfileViewModel


val CardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF050A30), Color(0xFF0A1450))
)
val GoldColor = Color(0xFFFFD700)

@Composable
fun ProfileScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    // Load dữ liệu user thật từ API
    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUser()
    }
    val userState by profileViewModel.userState.collectAsState()

    val displayName = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.fullName
        else -> "Loading..."
    }
    val displayLocation = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.location ?: "Unknown"
        else -> ""
    }
    val displayRole = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.role ?: "Player"
        else -> "Player"
    }
    val displayAvatarUrl = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.avatarUrl ?: ""
        else -> ""
    }
    val displayEmail = when (userState) {
        is UiState.Success -> (userState as UiState.Success).data.email
        else -> ""
    }
    val displayId = when (userState) {
        is UiState.Success -> "PKL-${(userState as UiState.Success).data.id}"
        else -> "PKL-..."
    }
    Scaffold(
        bottomBar = {
            BottomNav(
                selectedTab = HomeTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.HOME -> navController.navigateToTab(Routes.HOME)
                        HomeTab.MATCHES -> navController.navigateToTab(Routes.FIND_MATCH)
                        HomeTab.COURTS -> navController.navigateToTab(Routes.FIND_COURT)
                        HomeTab.BOOKINGS -> navController.navigateToTab(Routes.MY_BOOKINGS)
                        HomeTab.PROFILE -> { }
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MeshBackground()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp)
            ) {
                // 1. Header: Avatar + Info + Settings
                item { ProfileHeaderSection(
                    displayName = displayName,
                    displayLocation = displayLocation,
                    displayRole = displayRole,
                    displayAvatarUrl = displayAvatarUrl,
                    displayId = displayId
                ) }

                // 2. Leaderboard Banner
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        LeaderboardBanner(onClick = { navController.navigate(Routes.LEADERBOARD) })
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                // 3. Quick Actions (4 columns)
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        QuickActionsGrid(navController)
                    }
                }

                item { Spacer(modifier = Modifier.height(28.dp)) }

                // 4. Season Stats
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SeasonStatsSection()
                    }
                }

                item { Spacer(modifier = Modifier.height(28.dp)) }

                // 5. Prestige Titles
                item { EquippedTitlesSection() }

                item { Spacer(modifier = Modifier.height(28.dp)) }

                // 6. Account
                item {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AccountSection()
                    }
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

// ─── 1. Profile Header ───────────────────────────────────────────────────────

@Composable
fun ProfileHeaderSection(
    displayName: String = "Loading...",
    displayLocation: String = "",
    displayRole: String = "Player",
    displayAvatarUrl: String = "",
    displayId: String = "PKL-..."
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ping")
    val pingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.75f, targetValue = 0f, label = "ping_alpha",
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Avatar + Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Avatar with gradient ring
            Box(modifier = Modifier.size(68.dp), contentAlignment = Alignment.BottomEnd) {
                // Glow
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(PrimaryGreen.copy(alpha = 0.25f), CircleShape)
                        .align(Alignment.Center)
                )
                // Gradient ring + image
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Brush.linearGradient(listOf(PrimaryGreen, SoftMint, Color.White)),
                            CircleShape
                        )
                        .padding(2.dp)
                        .align(Alignment.Center)
                ) {
                    AsyncImage(
                        model = displayAvatarUrl.ifEmpty { "https://ui-avatars.com/api/?name=${displayName}&background=0D8ABC&color=fff&size=128" },
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .background(NavyDeep),
                        contentScale = ContentScale.Crop
                    )
                }
                // Verified badge
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(NavyDeep, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Name, location, badges
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = NavyDeep,
                    lineHeight = 22.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NavyDeep.copy(0.6f),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        displayLocation,
                        fontSize = 12.sp,
                        color = NavyDeep.copy(0.6f),
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ID badge
                    Box(
                        modifier = Modifier
                            .background(NavyDeep.copy(0.06f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            displayId,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep
                        )
                    }
                    // PRO badge
                    Box(
                        modifier = Modifier
                            .background(PrimaryGreen.copy(0.12f), RoundedCornerShape(6.dp))
                            .border(1.dp, PrimaryGreen.copy(0.25f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(7.dp), contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(PrimaryGreen.copy(pingAlpha), CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .background(PrimaryGreen, CircleShape)
                                )
                            }
                            Text(
                                displayRole.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = NavyDeep,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // Settings icon
        IconButton(
            onClick = { },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = NavyDeep)
        }
    }
}

// ─── 2. Leaderboard Banner ───────────────────────────────────────────────────

@Composable
fun LeaderboardBanner(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = NavyDeep.copy(0.2f))
            .clip(RoundedCornerShape(14.dp))
            .background(NavyDeep)
            .clickable { onClick() }
    ) {
        // Green gradient tint on left
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(PrimaryGreen.copy(0.18f), PrimaryGreen.copy(0.04f), Color.Transparent)
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                        .border(1.dp, Color.White.copy(0.05f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Leaderboard, contentDescription = null, tint = PrimaryGreen)
                }
                Column {
                    Text(
                        "GLOBAL RANK",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen.copy(0.85f),
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        "#42 Top Contender",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("VIEW", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(0.4f), letterSpacing = 1.sp)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ─── 3. Quick Actions Grid (4 columns) ───────────────────────────────────────

@Composable
fun QuickActionsGrid(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionTile(label = "Wallet", icon = Icons.Outlined.AccountBalanceWallet, modifier = Modifier.weight(1f)) {
            navController.navigate(Routes.WALLET)
        }
        QuickActionTile(label = "Bookings", icon = Icons.Outlined.CalendarMonth, modifier = Modifier.weight(1f)) {
            navController.navigate(Routes.MY_BOOKINGS)
        }
        QuickActionTile(label = "History", icon = Icons.Outlined.History, modifier = Modifier.weight(1f)) {
            navController.navigate(Routes.MATCH_HISTORY)
        }
        QuickActionTile(label = "Awards", icon = Icons.Outlined.EmojiEvents, modifier = Modifier.weight(1f)) {
            navController.navigate(Routes.LEADERBOARD)
        }
    }
}

@Composable
fun QuickActionTile(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier.aspectRatio(1f),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            border = BorderStroke(1.dp, NavyDeep.copy(0.1f)),
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = NavyDeep, modifier = Modifier.size(24.dp))
            }
        }
        Text(
            label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.55f),
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── 4. Season Stats ─────────────────────────────────────────────────────────

@Composable
fun SeasonStatsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "SEASON 24 STATS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDeep.copy(0.4f),
                letterSpacing = 1.sp
            )
            Box(
                modifier = Modifier
                    .background(PrimaryGreen.copy(0.12f), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("LIVE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = PrimaryGreen)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Matches
            StatCard(
                icon = Icons.Default.SportsTennis,
                value = "82",
                label = "Matches",
                modifier = Modifier.weight(1f)
            )
            // Win Rate
            StatCard(
                icon = Icons.Default.Percent,
                value = "68%",
                label = "Win Rate",
                modifier = Modifier.weight(1f)
            )
            // Win / Loss stacked
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStatCard(value = "56", label = "Wins", dotColor = PrimaryGreen)
                MiniStatCard(value = "26", label = "Loss", dotColor = Color(0xFFF87171))
            }
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = CoolGray.copy(0.3f),
        border = BorderStroke(1.dp, Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = NavyDeep.copy(0.3f), modifier = Modifier.size(20.dp))
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Black, color = NavyDeep, lineHeight = 28.sp)
            Text(label.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 0.8.sp)
        }
    }
}

@Composable
fun MiniStatCard(value: String, label: String, dotColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = CoolGray.copy(0.3f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = NavyDeep)
                Text(label.uppercase(), fontSize = 7.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f), letterSpacing = 0.8.sp)
            }
            Box(modifier = Modifier.size(7.dp).background(dotColor, CircleShape))
        }
    }
}

// ─── 5. Prestige Titles ───────────────────────────────────────────────────────

@Composable
fun EquippedTitlesSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "aura")
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1f, label = "aura_alpha",
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOut), RepeatMode.Reverse)
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "PRESTIGE TITLES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDeep.copy(0.4f),
                letterSpacing = 1.sp
            )
            Text(
                "VIEW ALL",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TitleBadge(
                    name = "Champion",
                    icon = Icons.Default.EmojiEvents,
                    isActive = true,
                    auraAlpha = auraAlpha
                )
            }
            item {
                TitleBadge(name = "Hot Streak", icon = Icons.Default.LocalFireDepartment, isActive = false)
            }
            item {
                TitleBadge(name = "Power Serve", icon = Icons.Default.Bolt, isActive = false)
            }
            item {
                TitleBadge(name = "Locked", icon = Icons.Default.Lock, isActive = false, isLocked = true)
            }
        }
    }
}

@Composable
fun TitleBadge(
    name: String,
    icon: ImageVector,
    isActive: Boolean,
    isLocked: Boolean = false,
    auraAlpha: Float = 1f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .width(88.dp)
            .then(if (isLocked) Modifier.alpha(0.5f) else Modifier)
    ) {
        Box(
            modifier = Modifier.size(76.dp),
            contentAlignment = Alignment.Center
        ) {
            // Aura glow background
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(PrimaryGreen.copy(0.35f * auraAlpha), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
            }

            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(14.dp),
                color = if (isLocked) CoolGray else if (isActive) CoolGray.copy(0.35f) else Color.White,
                border = BorderStroke(1.dp, if (isActive) CoolGray else CoolGray),
                shadowElevation = if (isActive) 0.dp else 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (isLocked) NavyDeep.copy(0.3f) else NavyDeep,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Active dot indicator
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 2.dp)
                        .background(PrimaryGreen, CircleShape)
                        .shadow(4.dp, CircleShape, spotColor = PrimaryGreen)
                )
            }
        }

        Text(
            name.uppercase(),
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Black else FontWeight.Bold,
            color = if (isActive) NavyDeep else NavyDeep.copy(0.55f),
            textAlign = TextAlign.Center,
            letterSpacing = 0.8.sp,
            lineHeight = 13.sp
        )
    }
}

// ─── 6. Account Section ──────────────────────────────────────────────────────

@Composable
fun AccountSection() {
    Column {
        Text(
            "ACCOUNT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.4f),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            border = BorderStroke(1.dp, CoolGray.copy(0.7f)),
            shadowElevation = 1.dp
        ) {
            Column {
                AccountItem(title = "Settings", icon = Icons.Outlined.Tune)
                HorizontalDivider(color = CoolGray.copy(0.6f), thickness = 0.8.dp)
                AccountItem(title = "Notifications", icon = Icons.Outlined.Notifications)
            }
        }
    }
}

@Composable
fun AccountItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(CoolGray.copy(0.5f), RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(18.dp))
            }
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = NavyDeep)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = NavyDeep.copy(0.3f))
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

fun Modifier.blur(radius: androidx.compose.ui.unit.Dp) = this.then(
    Modifier.drawBehind { drawRect(Color.Transparent) }
)

@Composable
fun MeshBackground() {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(SoftMint.copy(0.55f), Color.Transparent),
                        center = Offset(1200f, 0f),
                        radius = 900f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(CoolGray.copy(0.5f), Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = 700f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryGreen.copy(0.12f), Color.Transparent),
                        center = Offset(400f, 2200f),
                        radius = 800f
                    )
                )
        )
    }
}