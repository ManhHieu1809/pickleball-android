package com.example.pickleball.ui.screens.court.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pickleball.ui.theme.*


@Composable
fun LargeCourtCard(
    title: String,
    rating: String,
    reviewCount: String,
    distance: String,
    price: String,
    isIndoor: Boolean,
    tags: List<String>,
    imageUrl: String,
    primaryButtonText: String = "Book Now",
    secondaryButtonText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight), // Giả lập màu nền card xám nhẹ
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().background(Color.Gray)
                )

                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Star, null, tint = Color(0xFFFF8A00), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$rating ($reviewCount)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                    }
                }

                // Indoor/Outdoor Badge (Top Right)
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    color = if(isIndoor) NavyDeep else PrimaryGreen
                ) {
                    Text(
                        text = if(isIndoor) "Indoor" else "Outdoor",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(isIndoor) Color.White else NavyDeep
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, null, tint = TextSecondaryLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(distance, fontSize = 13.sp, color = TextSecondaryLight)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(price, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                        if (price != "Free") {
                            Text("/hr", fontSize = 12.sp, color = TextSecondaryLight)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Surface(color = AccentMint, shape = RoundedCornerShape(6.dp)) {
                            Text(tag, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (secondaryButtonText != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onClick,
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = NavyDeep),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray) // Sửa lỗi BorderStroke
                        ) {
                            Text("View Details", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(secondaryButtonText, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(primaryButtonText, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CompactCourtCard(
    title: String,
    price: String,
    distance: String,
    statusText: String,
    statusColor: Color,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Surface(color = Color.White, shape = RoundedCornerShape(6.dp)) {
                        Text(price, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(distance, fontSize = 12.sp, color = TextSecondaryLight)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(statusText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("View", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}