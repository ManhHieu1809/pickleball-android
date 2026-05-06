package com.example.pickleball.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PrimaryCyan = Color(0xFF00f684)
val BackgroundWhite = Color(0xFFFFFFFF)
val SurfaceGray = Color(0xFFE8EBF0)
val OnBackgroundNavy = Color(0xFF050A30)
val AccentMint = Color(0xFFD6FFF3)
val OutlineGray = Color(0xFFB0B8C1)
val DangerRed = Color(0xFFDC2626)
val DangerRedBg = Color(0xFFFEF2F2)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        containerColor = BackgroundWhite,
        topBar = { SettingsTopAppBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 100.dp) // Trừ hao khoảng trống cho footer
            ) {
                UserProfileHero()

                Spacer(modifier = Modifier.height(32.dp))

                AccountProfileSection()
                Spacer(modifier = Modifier.height(24.dp))

                SupportInfoSection()
                Spacer(modifier = Modifier.height(24.dp))

                DangerZoneSection()
            }

            // Sticky Footer Gradient
            SettingsFooter(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun SettingsSectionWrapper(
    title: String,
    titleColor: Color = OnBackgroundNavy.copy(alpha = 0.6f),
    containerBorder: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceGray),
            border = containerBorder,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    iconTint: Color = OnBackgroundNavy,
    textColor: Color = OnBackgroundNavy,
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = OnBackgroundNavy.copy(alpha = 0.4f)
        )
    }
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textColor)
            }
            trailingContent()
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = BackgroundWhite.copy(alpha = 0.5f),
                thickness = 1.dp
            )
        }
    }
}

// --- CÁC SECTIONS CHI TIẾT ---
@Composable
fun SettingsTopAppBar() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundWhite)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { /* TODO: Xử lý sự kiện back */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = OnBackgroundNavy)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SETTINGS",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = OnBackgroundNavy,
                    letterSpacing = (-0.5).sp
                )
            }
            IconButton(onClick = { /* TODO: Xử lý tìm kiếm */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = OnBackgroundNavy)
            }
        }
        HorizontalDivider(color = SurfaceGray, thickness = 1.dp)
    }
}

@Composable
fun UserProfileHero() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceGray, RoundedCornerShape(8.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Box
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(BackgroundWhite, CircleShape)
                .border(4.dp, PrimaryCyan, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color.DarkGray) // Mock avatar, thay bằng AsyncImage của Coil sau
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(
                text = "Alex Morgan",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = OnBackgroundNavy
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PRO TIER REFEREE",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = OnBackgroundNavy.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun AccountProfileSection() {
    SettingsSectionWrapper(title = "Account & Profile") {
        SettingsItemRow(icon = Icons.Outlined.Person, title = "Edit Profile")
        // Do đã xóa Linked Accounts nên Change Password là item cuối cùng -> showDivider = false
        SettingsItemRow(icon = Icons.Outlined.Lock, title = "Change Password", showDivider = false)
    }
}

@Composable
fun SupportInfoSection() {
    SettingsSectionWrapper(title = "Support & Info") {
        // Chỉ giữ lại Help Center, là item duy nhất nên showDivider = false
        SettingsItemRow(icon = Icons.Outlined.Info, title = "Help Center", showDivider = false) {
            Icon(Icons.AutoMirrored.Outlined.OpenInNew, null, tint = OnBackgroundNavy.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun DangerZoneSection() {
    SettingsSectionWrapper(
        title = "Danger Zone",
        titleColor = DangerRed,
        containerBorder = BorderStroke(1.dp, DangerRed.copy(alpha = 0.2f))
    ) {
        // Chỉ giữ lại Logout, là item duy nhất nên showDivider = false
        SettingsItemRow(
            icon = Icons.AutoMirrored.Outlined.ExitToApp,
            title = "Logout",
            showDivider = false,
            trailingContent = {
                Icon(Icons.AutoMirrored.Outlined.ExitToApp, null, tint = OnBackgroundNavy.copy(alpha = 0.4f))
            }
        )
    }
}

@Composable
fun SettingsFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, BackgroundWhite.copy(alpha = 0.95f), BackgroundWhite),
                    startY = 0f,
                    endY = 150f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SECURED BY REFEREE HUB PROTOCOL",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = OnBackgroundNavy.copy(alpha = 0.4f),
            letterSpacing = 1.5.sp,
            modifier = Modifier
                .background(SurfaceGray, CircleShape)
                .border(1.dp, OutlineGray.copy(alpha = 0.2f), CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}