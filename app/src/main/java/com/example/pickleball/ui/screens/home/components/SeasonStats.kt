package com.example.pickleball.ui.screens.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickleball.ui.theme.BackgroundLight
import com.example.pickleball.ui.theme.NavyDeep

@Composable
fun SeasonStats(matches: Int, winStreak: Int) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text("Season Stats", style = MaterialTheme.typography.titleLarge, color = NavyDeep)

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("MATCHES", matches.toString(), 0.65f, Icons.Default.SportsTennis, NavyDeep, Modifier.weight(1f))
            StatCard("WIN STREAK", winStreak.toString(), 0.4f, Icons.Default.LocalFireDepartment, Color(0xFFFF8A00), Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    progress: Float,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animated by animateFloatAsState(progress, label = "")

    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, NavyDeep.copy(0.05f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = NavyDeep.copy(0.5f))
            Icon(icon, null, tint = color)
        }

        Spacer(Modifier.height(8.dp))

        Text(value, style = MaterialTheme.typography.headlineMedium, color = NavyDeep)

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(6.dp).background(BackgroundLight, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(animated).background(color, RoundedCornerShape(50))
            )
        }
    }
}