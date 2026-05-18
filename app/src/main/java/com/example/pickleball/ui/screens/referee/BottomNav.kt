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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme

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
    val isDark = isSystemInDarkTheme()
    val glassColor = if (isDark) {
        Color(0xFF151515).copy(alpha = 0.85f)
    } else {
        Color.White.copy(alpha = 0.85f)
    }
    val borderColor = if (isDark) {
        Color.White.copy(alpha = 0.1f)
    } else {
        Color.Black.copy(alpha = 0.05f)
    }
    val shadowColor = if (isDark) Color.Black.copy(alpha = 0.8f) else Color.Gray.copy(alpha = 0.3f)

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
                    spotColor = shadowColor
                )
                .widthIn(max = 340.dp)
                .fillMaxWidth(),
            color = glassColor,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
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
    val primaryColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    val activeBg = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor else inactiveColor,
        label = "colorAnim"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) activeBg else Color.Transparent,
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
                        .background(primaryColor, CircleShape)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = primaryColor,
                            spotColor = primaryColor
                        )

                )
            }
        }
    }
}