package com.example.pickleball.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickleball.ui.theme.BackgroundLight
import com.example.pickleball.ui.theme.NavyDeep

data class MatchUiModel(val day: String, val date: String, val type: String, val time: String, val title: String, val location: String)

@Composable
fun UpNextSection(matches: List<MatchUiModel>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Up Next", style = MaterialTheme.typography.titleLarge, color = NavyDeep)
            Text("See All", style = MaterialTheme.typography.labelMedium, color = NavyDeep.copy(0.6f))
        }

        Spacer(Modifier.height(12.dp))

        matches.forEach { match ->
            MatchItem(match)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun MatchItem(match: MatchUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, NavyDeep.copy(0.05f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(48.dp)
                .background(BackgroundLight, RoundedCornerShape(8.dp))
                .padding(vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(match.day, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = NavyDeep.copy(0.6f))
            Text(match.date, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = NavyDeep)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${match.type.uppercase()} • ${match.time}",
                style = MaterialTheme.typography.labelSmall,
                color = NavyDeep.copy(0.5f)
            )

            Text(match.title, style = MaterialTheme.typography.titleSmall, color = NavyDeep)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = NavyDeep.copy(0.5f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(match.location, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = NavyDeep.copy(0.5f))
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = NavyDeep.copy(0.3f))
    }
}