package com.example.pickleball.ui.screens.court

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Shower
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pickleball.ui.theme.*

val sampleCourtImages = listOf(
    "https://picsum.photos/600/400?random=1",
    "https://picsum.photos/600/400?random=2",
    "https://picsum.photos/600/400?random=3"
)

@Composable
fun CourtDetailScreen(
    courtId: String?,
    onBackClick: () -> Unit,
    onCalendarClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp)
        ) {
            HeroImageCarousel(images = sampleCourtImages)
            Column(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                HeaderTitleSection()
                Spacer(modifier = Modifier.height(12.dp))
                RatingAndTagsSection()

                Spacer(modifier = Modifier.height(24.dp))

                LocationMapCard(
                    address = "123 Rally Drive",
                    distance = "Austin, TX • 2.4 miles away"
                )
                Spacer(modifier = Modifier.height(24.dp))
                AmenitiesSection()
                Spacer(modifier = Modifier.height(24.dp))
                TimeSelectionSection(onCalendarClick = onCalendarClick)
                Spacer(modifier = Modifier.height(20.dp))
                BookingSlotsSection()
            }
        }

        TopNavOverlay(onBackClick = onBackClick)
        BottomBookingBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageCarousel(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = Modifier.height(350.dp).fillMaxWidth()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().background(Color.LightGray)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 500f
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 42.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) PrimaryGreen else Color.White.copy(alpha = 0.6f)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun LocationMapCard(address: String, distance: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { /* Mở Google Maps */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_map),
                contentDescription = "Map background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().background(Color.Gray)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(NavyDeep.copy(alpha = 0.9f), Color.Transparent)
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icon Location trong vòng tròn mờ
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.LocationOn, null, tint = PrimaryGreen)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(address, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(distance, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                    }
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun HeaderTitleSection() {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Ace Pickleball Club\nCourt 1", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(SurfaceCardColor, RoundedCornerShape(8.dp)).padding(8.dp)
        ) {
            Text("ELO", fontSize = 10.sp, color = TextSecondaryLight)
            Text("4.5+", fontWeight = FontWeight.Black, color = NavyDeep)
        }
    }
}

@Composable
fun RatingAndTagsSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Rounded.Star, null, tint = PrimaryGreen)
        Text(" 4.9 ", fontWeight = FontWeight.Bold, color = NavyDeep)
        Text("(120 Reviews)", color = TextSecondaryLight, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Surface(color = AccentMint, shape = RoundedCornerShape(4.dp)) {
            Text("Pro Surface", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 11.sp, color = NavyDeep)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("• Indoor", color = TextSecondaryLight, fontSize = 12.sp)
    }
}

@Composable
fun AmenitiesSection() {
    Text("Amenities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        AmenityItem("Lights", Icons.Outlined.LightMode)
        AmenityItem("A/C", Icons.Outlined.AcUnit)
        AmenityItem("Parking", Icons.Outlined.LocalParking)
        AmenityItem("Showers", Icons.Outlined.Shower)
    }
}

@Composable
fun TimeSelectionSection(onCalendarClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Select Time", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onCalendarClick() }) {
            Text("Calendar", color = PrimaryGreenDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Outlined.CalendarMonth, null, tint = PrimaryGreenDark, modifier = Modifier.size(20.dp))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        DateItem("Today", "24", true)
        Spacer(modifier = Modifier.width(12.dp))
        DateItem("Fri", "25", false)
        Spacer(modifier = Modifier.width(12.dp))
        DateItem("Sat", "26", false)
        Spacer(modifier = Modifier.width(12.dp))
        DateItem("Sun", "27", false)
    }
}

@Composable
fun BookingSlotsSection() {
    SlotItem("06:00 PM - 07:00 PM", null, "Booked", false)
    Spacer(modifier = Modifier.height(12.dp))
    SlotItem("07:00 PM - 08:00 PM", "$45.00", "4 spots left", true)
    Spacer(modifier = Modifier.height(12.dp))
    SlotItem("08:00 PM - 09:00 PM", "$45.00", "Night Lighting", false)
}

@Composable
fun TopNavOverlay(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.background(Color.Black.copy(0.3f), CircleShape).border(1.dp, Color.White.copy(0.2f), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        }
        IconButton(
            onClick = {},
            modifier = Modifier.background(Color.Black.copy(0.3f), CircleShape).border(1.dp, Color.White.copy(0.2f), CircleShape)
        ) {
            Icon(Icons.Filled.Favorite, null, tint = Color.White)
        }
    }
}

@Composable
fun BottomBookingBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("TOTAL PRICE", fontSize = 10.sp, color = TextSecondaryLight, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$45.00", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                    Text("/ 1 hour", fontSize = 12.sp, color = TextSecondaryLight, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Book Slot", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
            }
        }
    }
}

@Composable
fun AmenityItem(name: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .background(SurfaceCardColor, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = NavyDeep)
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DateItem(day: String, date: String, isSelected: Boolean) {
    val bgColor = if(isSelected) PrimaryGreen else SurfaceCardColor
    val textColor = if(isSelected) NavyDeep else TextSecondaryLight

    Column(
        modifier = Modifier
            .size(width = 72.dp, height = 84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(if(isSelected) 2.dp else 0.dp, if(isSelected) PrimaryGreenDark else Color.Transparent, RoundedCornerShape(16.dp))
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(day.uppercase(), fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(date, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
    }
}

@Composable
fun SlotItem(time: String, price: String?, subtext: String, isSelected: Boolean) {
    val borderColor = if(isSelected) PrimaryGreen else Color.Transparent
    val bgColor = if(isSelected) Color.White else SurfaceCardColor
    val shadowElevation = if(isSelected) 4.dp else 0.dp

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(if(isSelected) 2.dp else 1.dp, if(isSelected) PrimaryGreen else SurfaceCardColor),
        color = bgColor,
        shadowElevation = shadowElevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(time, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtext, fontSize = 13.sp, color = if(isSelected) PrimaryGreenDark else TextSecondaryLight, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium)
            }

            if (price != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(price, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NavyDeep)
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryGreen else Color.Transparent)
                            .border(2.dp, if (isSelected) PrimaryGreen else Color.LightGray.copy(0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Rounded.Check, null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            } else {
                Surface(color = Color.Black.copy(0.05f), shape = RoundedCornerShape(6.dp)) {
                    Text("Booked", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 12.sp, color = TextSecondaryLight, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}