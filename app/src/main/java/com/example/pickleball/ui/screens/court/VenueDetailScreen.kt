package com.example.pickleball.ui.screens.court

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Shower
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.pickleball.data.model.Court
import com.example.pickleball.data.model.UiState
import com.example.pickleball.data.model.Venue
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.BookingViewModel
import com.example.pickleball.viewmodel.VenueViewModel

@Composable
fun VenueDetailScreen(
    venueId: String?,
    onBackClick: () -> Unit,
    onCourtClick: (String) -> Unit,
    venueViewModel: VenueViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    // Load venue thật từ API
    LaunchedEffect(venueId) {
        venueId?.toLongOrNull()?.let { id ->
            venueViewModel.loadVenueById(id)
            bookingViewModel.loadCourtsByVenue(id)
        }
    }

    val venueState by venueViewModel.venueDetailState.collectAsState()
    val courtsState by bookingViewModel.courtsState.collectAsState()

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        when (venueState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is UiState.Success -> {
                val venue = (venueState as UiState.Success<Venue>).data
                val images = listOf(
                    venue.imageUrl ?: "https://picsum.photos/600/400?random=1",
                    "https://picsum.photos/600/400?random=${venue.id}_2",
                    "https://picsum.photos/600/400?random=${venue.id}_3"
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 0.dp)
                ) {
                    HeroImageCarousel(images = images)
                    Column(
                        modifier = Modifier
                            .offset(y = (-30).dp)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .background(Color.White)
                            .padding(20.dp)
                    ) {
                        HeaderTitleSection(
                            venueName = venue.name ?: "Unknown Venue",
                            rating = venue.rating?.toString() ?: "N/A"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        RatingAndTagsSection(
                            rating = venue.rating?.toString() ?: "N/A",
                            reviewCount = venue.reviewCount?.toString() ?: "0"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        LocationMapCard(
                            address = venue.address ?: "Unknown",
                            distance = venue.description ?: ""
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        AmenitiesSection()
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Available Courts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Show Courts from bookingViewModel
                        when (courtsState) {
                            is UiState.Loading -> CircularProgressIndicator(color = PrimaryGreen)
                            is UiState.Success -> {
                                val courts = (courtsState as UiState.Success<List<Court>>).data
                                if (courts.isEmpty()) {
                                    Text("No courts available for this venue", color = TextSecondaryLight)
                                } else {
                                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                        courts.forEach { court ->
                                            CourtItem(court = court, onClick = { onCourtClick(court.id.toString()) })
                                        }
                                    }
                                }
                            }
                            is UiState.Error -> Text("Failed to load courts: ${(courtsState as UiState.Error).message}", color = Color.Red)
                            else -> {}
                        }
                    }
                }

                TopNavOverlay(onBackClick = onBackClick)
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load venue", fontWeight = FontWeight.Bold, color = NavyDeep)
                    Button(
                        onClick = {
                            venueId?.toLongOrNull()?.let { venueViewModel.loadVenueById(it) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Retry", color = NavyDeep, fontWeight = FontWeight.Bold)
                    }
                }
                TopNavOverlay(onBackClick = onBackClick)
            }
            else -> {}
        }
    }
}

@Composable
fun CourtItem(court: Court, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardColor),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(court.courtName ?: "Court", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                Spacer(modifier = Modifier.height(4.dp))
                Text(court.description ?: "Pickleball Court", fontSize = 13.sp, color = TextSecondaryLight)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${court.pricePerHour ?: 20}/hr",
                    fontWeight = FontWeight.Bold, 
                    fontSize = 16.sp, 
                    color = PrimaryGreenDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(color = if (court.isActive) PrimaryGreen else Color.Red.copy(0.1f), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        if (court.isActive) "Available" else "Maintenance", 
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageCarousel(images: List<String>) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { images.size })

    Box(modifier = Modifier.height(350.dp).fillMaxWidth()) {
        androidx.compose.foundation.pager.HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
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
fun HeaderTitleSection(venueName: String = "Court", rating: String = "N/A") {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(venueName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = NavyDeep, modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(SurfaceCardColor, RoundedCornerShape(8.dp)).padding(8.dp)
        ) {
            Text("Rating", fontSize = 10.sp, color = TextSecondaryLight)
            Text(rating, fontWeight = FontWeight.Black, color = NavyDeep)
        }
    }
}

@Composable
fun RatingAndTagsSection(rating: String = "N/A", reviewCount: String = "0") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Rounded.Star, null, tint = PrimaryGreen)
        Text(" $rating ", fontWeight = FontWeight.Bold, color = NavyDeep)
        Text("($reviewCount Reviews)", color = TextSecondaryLight, fontSize = 12.sp)
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
