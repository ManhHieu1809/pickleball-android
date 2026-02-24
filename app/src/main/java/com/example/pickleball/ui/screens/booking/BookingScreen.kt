package com.example.pickleball.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*
import java.nio.file.WatchEvent

@Composable
fun BookingScreen(
    navController: NavController,
    onBackClick: () -> Unit
){
    Scaffold(
        containerColor = Color.White,
        topBar = {
            BookingTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            BookingBottomBar(totalPrice = "$20.00", navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .background(
                       Brush.radialGradient(
                           colors = listOf(
                               AccentMint.copy(alpha = 0.6f),
                               Color.Transparent
                           ),
                           center = androidx.compose.ui.geometry.Offset(1000f, 0f),
                            radius = 500f
                       )
                   )
           ) {
               Column {
                   MonthHeaderSection()
                   DateStripSection()
               }
           }
            CourtInfoCard()

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TimeSlotSection(
                    title = "Morning",
                    icon = Icons.Default.WbTwilight,
                    slots = listOf(
                        TimeSlotData("07:00 AM", "$15", SlotStatus.AVAILABLE),
                        TimeSlotData("08:00 AM", "$15", SlotStatus.AVAILABLE),
                        TimeSlotData("09:00 AM", "Booked", SlotStatus.BOOKED),
                        TimeSlotData("10:00 AM", "$20", SlotStatus.SELECTED), // Selected item
                        TimeSlotData("11:00 AM", "$20", SlotStatus.AVAILABLE)
                    )
                )
                TimeSlotSection(
                    title = "Afternoon",
                    icon = Icons.Default.WbSunny,
                    slots = listOf(
                        TimeSlotData("12:00 PM", "$25", SlotStatus.AVAILABLE),
                        TimeSlotData("01:00 PM", "$25", SlotStatus.AVAILABLE),
                        TimeSlotData("02:00 PM", "$25", SlotStatus.AVAILABLE),
                        TimeSlotData("03:00 PM", "League", SlotStatus.DISABLED),
                        TimeSlotData("04:00 PM", "League", SlotStatus.DISABLED)
                    )
                )

                TimeSlotSection(
                    title = "Evening",
                    icon = Icons.Default.Bedtime,
                    slots = listOf(
                        TimeSlotData("06:00 PM", "$30", SlotStatus.AVAILABLE),
                        TimeSlotData("07:00 PM", "$30", SlotStatus.AVAILABLE)
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

enum class SlotStatus{AVAILABLE, BOOKED, SELECTED, DISABLED}
data class TimeSlotData(val time: String, val priceOrLabel: String, val status: SlotStatus)

@Composable
fun BookingTopBar(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
            }
            Text(
                text = "Select Time",
                style = MaterialTheme.typography.titleMedium, // Lexend
                fontWeight = FontWeight.Bold,
                color = NavyDeep
            )
            Spacer(modifier = Modifier.width(48.dp)) // Dummy spacer to center title
        }
    }
}

@Composable
fun MonthHeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "September 2023",
            style = MaterialTheme.typography.headlineSmall, // Lexend
            color = NavyDeep
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { }
        ) {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.labelMedium,
                color = NavyDeep
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = NavyDeep,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun DateStripSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Selected Date (Wednesday 20)
        DateItem(day = "Wed", date = "20", isSelected = true)

        // Other Dates
        DateItem(day = "Thu", date = "21", isSelected = false)
        DateItem(day = "Fri", date = "22", isSelected = false)
        DateItem(day = "Sat", date = "23", isSelected = false)
        DateItem(day = "Sun", date = "24", isSelected = false)
        DateItem(day = "Mon", date = "25", isSelected = false)
    }
}

@Composable
fun DateItem(day: String, date: String, isSelected: Boolean) {
    val bgColor = if(isSelected) PrimaryGreen else SurfaceCardColor
    val textColor = if(isSelected) NavyDeep else NavyDeep
    val dayColor = if(isSelected) NavyDeep.copy(0.9f) else NavyDeep.copy(0.7f)
    val shadowElevation = if(isSelected) 8.dp else 0.dp

    Column(
        modifier = Modifier
            .width(56.dp)
            .height(64.dp)
            .shadow(shadowElevation, RoundedCornerShape(8.dp), spotColor = PrimaryGreen.copy(0.5f))
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { }
            .border(
                width = if (isSelected) 1.dp else 1.dp,
                color = if (isSelected) PrimaryGreen else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = dayColor
        )
        Text(
            text = date,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            color = textColor
        )
    }
}

@Composable
fun CourtInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDMUxe4iR1t-QYx8zyG1BCbxlrkwqX884KnnGTyY6nuyK_Dx7V_S7nmhjWXYiq8eS_wn8npCPr3VYBO1IvSxe-Kevc-Inp7xKS6lxECmHPFjiCRQ3SVEd2tqo4lVE3TedNl63L6VeUdJfvSPKHvR_Qgpiut41xjBzZQS6UGKz0N_TbXu0OrE4bpfJvJbUpK10GXqn62t9Oz3pVPGSZm1uIp_VxupKmEDPnf1VSsbkSeKRHNBeh8rMlREgEdPRp_vSm5fPtRBdhEEyZr", // URL from HTML
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Riverside Courts",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Court 3 • Hard Surface",
                    style = MaterialTheme.typography.bodySmall,
                    color = NavyDeep.copy(0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = NavyDeep,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$20/hr",
                    style = MaterialTheme.typography.labelLarge,
                    color = NavyDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Change",
                    style = MaterialTheme.typography.labelSmall,
                    color = NavyDeep,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun TimeSlotSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, slots: List<TimeSlotData>) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NavyDeep,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                letterSpacing = 1.sp
            )
        }

        val chunkedSlots = slots.chunked(3)
        chunkedSlots.forEachIndexed { index, rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSlots.forEach { slot ->
                    Box(modifier = Modifier.weight(1f)) {
                        TimeSlotCard(slot)
                    }
                }
                if (rowSlots.size < 3) {
                    repeat(3 - rowSlots.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            if (index < chunkedSlots.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TimeSlotCard(data: TimeSlotData) {
    val isSelected = data.status == SlotStatus.SELECTED
    val isBookedOrDisabled = data.status == SlotStatus.BOOKED || data.status == SlotStatus.DISABLED

    val bgColor = if (isSelected) AccentMint else SurfaceCardColor
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val textColor = if (isBookedOrDisabled) NavyDeep.copy(0.5f) else NavyDeep
    val priceColor = if (isBookedOrDisabled) NavyDeep.copy(0.5f) else NavyDeep.copy(0.7f)
    val textDecoration = if (isBookedOrDisabled) TextDecoration.LineThrough else null

    Box(
        modifier = Modifier.height(60.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = !isBookedOrDisabled) { },
            shape = RoundedCornerShape(8.dp),
            color = bgColor,
            border = BorderStroke(if(isSelected) 2.dp else 1.dp, borderColor),
            shadowElevation = if(isSelected) 4.dp else 0.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = data.time,
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textDecoration = textDecoration
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = data.priceOrLabel,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if(isSelected) NavyDeep else priceColor
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(20.dp)
                    .background(PrimaryGreen, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = NavyDeep,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun BookingBottomBar(totalPrice: String, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(0.95f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, SurfaceCardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding(), // Safe area
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total amount",
                    style = MaterialTheme.typography.labelSmall,
                    color = NavyDeep,
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = totalPrice,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 24.sp,
                        color = NavyDeep
                    )
                    Text(
                        text = " / 1h",
                        style = MaterialTheme.typography.bodySmall,
                        color = NavyDeep.copy(0.7f),
                        modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Button(
                onClick = { navController.navigate(Routes.PAYMENT_CONFIRMATION) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = NavyDeep
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Proceed to Payment",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}