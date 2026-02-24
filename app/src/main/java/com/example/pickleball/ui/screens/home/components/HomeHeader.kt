package com.example.pickleball.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pickleball.ui.theme.NavyDeep
import com.example.pickleball.ui.theme.PrimaryGreen

@Composable
fun HomeHeader(
    userName: String,
    avatarUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .background(PrimaryGreen, CircleShape)
                        .border(2.5.dp, Color.White, CircleShape)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Hi, $userName",
                    style = MaterialTheme.typography.titleLarge,
                    color = NavyDeep,
                    lineHeight = 24.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "PRO MEMBER",
                        style = MaterialTheme.typography.labelSmall,
                        color = NavyDeep.copy(alpha = 0.5f),
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
                .shadow(4.dp, CircleShape, spotColor = NavyDeep.copy(alpha = 0.1f))
                .border(1.dp, NavyDeep.copy(alpha = 0.05f), CircleShape)
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = NavyDeep,
                    modifier = Modifier.size(24.dp)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 1.dp, y = (-1).dp)
                        .background(PrimaryGreen, CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                )
            }
        }
    }
}