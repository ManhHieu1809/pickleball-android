package com.example.pickleball.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pickleball.ui.theme.NavyDeep
import com.example.pickleball.ui.theme.PrimaryGreen

data class CourtUiModel(val name: String, val distance: String, val status: String, val imageUrl: String)

@Composable
fun NearbyCourts(courts: List<CourtUiModel>) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sửa font tiêu đề
            Text("Nearby Courts", style = MaterialTheme.typography.titleLarge, color = NavyDeep)
            Text("Map View", style = MaterialTheme.typography.labelMedium, color = NavyDeep.copy(0.6f))
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courts) { court -> CourtCard(court) }
        }
    }
}

@Composable
private fun CourtCard(court: CourtUiModel) {
    Box(
        modifier = Modifier.width(240.dp).height(140.dp).clip(RoundedCornerShape(14.dp))
    ) {
        AsyncImage(
            model = court.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, NavyDeep.copy(0.9f))))
        )

        Column(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)) {
            // Sửa font tên sân
            Text(court.name, style = MaterialTheme.typography.titleSmall, color = Color.White)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.NearMe, null, tint = Color.White.copy(0.8f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(court.distance, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = Color.White.copy(0.8f))
            }
        }

        Text(
            text = court.status,
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryGreen,
            modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
        )
    }
}