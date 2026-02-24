package com.example.pickleball.ui.screens.home.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickleball.ui.theme.NavyDeep
import com.example.pickleball.ui.theme.PrimaryGreen

@Composable
fun QuickActions(
    onJoinRanked: () -> Unit = {},
    onBookCourt: () -> Unit = {},
    onFindCasual: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(16.dp, RoundedCornerShape(16.dp), ambientColor = PrimaryGreen.copy(0.5f), spotColor = PrimaryGreen.copy(0.6f))
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(PrimaryGreen, Color(0xFF00D672))))
                .clickable { onJoinRanked() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SportsTennis, null, tint = NavyDeep, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
                // Sửa font
                Text("JOIN RANKED QUEUE", style = MaterialTheme.typography.labelLarge, color = NavyDeep, letterSpacing = 1.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SmallAction("BOOK COURT", Icons.Default.CalendarMonth, onBookCourt, Modifier.weight(1f))
            SmallAction("FIND CASUAL", Icons.Default.Search, onFindCasual, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SmallAction(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(84.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = NavyDeep.copy(0.1f))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, NavyDeep.copy(0.05f), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = NavyDeep, modifier = Modifier.size(26.dp))
        Spacer(Modifier.height(8.dp))
        // Sửa font
        Text(text, style = MaterialTheme.typography.labelSmall, color = NavyDeep.copy(0.8f))
    }
}