package com.example.pickleball.ui.screens.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

val NeonGreen = Color(0xFF00E677)
val DarkGlass = Color(0xFF151515).copy(alpha = 0.85f)
val BorderWhite10 = Color.White.copy(alpha = 0.1f)
val ActiveBackground = Color.White.copy(alpha = 0.05f)
val InactiveGray = Color(0xFF6B7280)

enum class HomeTab(val label: String, val iconOn: ImageVector, val iconOff: ImageVector) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    MATCHES("Matches", Icons.Filled.SportsTennis, Icons.Outlined.SportsTennis),
    BOOKINGS("Bookings", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    COURTS("Courts", Icons.Filled.GridView, Icons.Outlined.GridView),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun BottomNav(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.8f)
                )
                .widthIn(max = 340.dp)
                .fillMaxWidth(),
            color = DarkGlass,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderWhite10)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomeTab.values().forEach { tab ->
                    NavCapsuleItem(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { onTabSelected(tab) }
                    )
                }
            }
        }
    }
}

@Composable
fun NavCapsuleItem(
    tab: HomeTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) NeonGreen else InactiveGray,
        label = "colorAnim"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) ActiveBackground else Color.Transparent,
        label = "bgAnim"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) tab.iconOn else tab.iconOff,
                contentDescription = tab.label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(NeonGreen, CircleShape)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = NeonGreen,
                            spotColor = NeonGreen
                        )

                )
            }
        }
    }
}