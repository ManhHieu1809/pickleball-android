package com.example.pickleball.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.pickleball.ui.theme.*

// Sử dụng lại màu từ theme (hoặc định nghĩa local nếu chưa có trong Color.kt)
val CoolGray = Color(0xFFE8EBF0)
val SoftMint = Color(0xFFD6FFF3)

@Composable
fun PaymentConfirmationScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = { PaymentTopBar(onBackClick) },
        bottomBar = { PaymentBottomBar(onConfirmClick) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient (Soft Mint fade) giống HTML
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(SoftMint.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Booking Summary
                BookingSummarySection()

                // 2. Cost Breakdown
                CostBreakdownCard()

                // 3. Payment Method
                PaymentMethodSection()

                // Spacer để nội dung không bị che bởi BottomBar khi scroll hết
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- Components ---

@Composable
fun PaymentTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
                .border(1.dp, CoolGray.copy(alpha = 0.5f), CircleShape)
                .shadow(1.dp, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "Payment Confirmation",
            style = MaterialTheme.typography.titleLarge,
            color = NavyDeep,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(end = 40.dp), // Padding end để cân giữa title
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BookingSummarySection() {
    Column {
        Text(
            text = "BOOKING SUMMARY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = NavyDeep.copy(alpha = 0.8f),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, CoolGray)
        ) {
            Column {
                // Header Image
                Box(modifier = Modifier.height(144.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCEmW-NlL-to-VDy26TdB-kZgpneJB4h9ULHFRd6bDFc90mMP4CF0aPI1N_ZpREispUyg8HAtREbYVPsihhWqifUZTJ4vReKGL0smmFRrcxcaj3JaOouvaLRAFjK8njs8dneFDkfJ-9LDoJTl4zdt8GtgLWtjnYCzzB6lxIMJ9kpWOpIPqf5AgWyvMHEH8mWAK5Gt3xWRVtDO-VHJFusS3LSIHEtYgpvVh1A4H0u9Z9_Guqza3Bhx6ZQYnxiwidk8Ryop7HhynwHrQ0", // URL từ HTML
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().background(Color.Gray)
                    )
                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, NavyDeep.copy(alpha = 0.8f)),
                                    startY = 100f
                                )
                            )
                    )
                    Text(
                        text = "Skyline Pickleball Center",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                    )
                }

                // Details
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Court 4 (Outdoor)",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = NavyDeep.copy(0.7f), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Downtown Sports Complex", style = MaterialTheme.typography.bodySmall, color = NavyDeep.copy(0.7f))
                            }
                        }

                        // Duration Badge
                        Surface(
                            color = SoftMint,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "1.5 hr",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyDeep,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = CoolGray)
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Date Info
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).background(SoftMint, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CalendarToday, null, tint = NavyDeep, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("DATE", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f))
                                Text("Nov 14, 2023", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                            }
                        }

                        // Time Info
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).background(SoftMint, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Schedule, null, tint = NavyDeep, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("TIME", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.6f))
                                Text("14:00 - 15:30", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CostBreakdownCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CoolGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            CostRow("Subtotal", "$40.00")
            CostRow("Service Fee", "$2.50")
            CostRow("Tax", "$2.50")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                Text("$45.00", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
            }
        }
    }
}

@Composable
fun CostRow(label: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .drawBehindLine(CoolGray), // Custom modifier giả lập border-b dashed (ở đây dùng solid cho đơn giản hoặc custom Canvas)
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.7f))
        Text(amount, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NavyDeep)
    }
}

// Extension function giả lập đường kẻ dưới (thay thế cho border-b trong HTML)
fun Modifier.drawBehindLine(color: Color) = this.then(
    Modifier.background(Color.Transparent) // Placeholder for simplicity, in real app can use Canvas to draw dashed line
)

@Composable
fun PaymentMethodSection() {
    // State quản lý phương thức thanh toán
    var selectedMethod by remember { mutableStateOf("Visa") }

    Column {
        Text(
            text = "PAYMENT METHOD",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = NavyDeep.copy(alpha = 0.8f),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PaymentOptionItem(
                title = "Visa ending in 4242",
                subtitle = "Expires 12/25",
                icon = Icons.Default.CreditCard,
                isSelected = selectedMethod == "Visa",
                onClick = { selectedMethod = "Visa" }
            )

            PaymentOptionItem(
                title = "Apple Pay",
                subtitle = "Linked to **** 8888",
                icon = Icons.Default.Smartphone,
                isSelected = selectedMethod == "ApplePay",
                onClick = { selectedMethod = "ApplePay" }
            )

            PaymentOptionItem(
                title = "PickleWallet",
                subtitle = "Balance: $120.00",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = selectedMethod == "Wallet",
                onClick = { selectedMethod = "Wallet" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* Add new method logic */ }
        ) {
            Icon(Icons.Default.AddCircle, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add new payment method", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
        }
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) SoftMint else CoolGray
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val hoverColor = if (isSelected) SoftMint else CoolGray.copy(0.8f) // Simulation

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = NavyDeep)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.6f))
        }

        // Custom Radio Indicator (HTML: radio-indicator)
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, NavyDeep.copy(0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                // Chấm tròn bên trong khi selected
                Box(
                    modifier = Modifier
                        .size(20.dp) // Full size background override
                        .background(PrimaryGreen, CircleShape)
                        .border(2.dp, PrimaryGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Box(modifier = Modifier.size(8.dp).background(NavyDeep, CircleShape))
                }
            }
        }
    }
}

@Composable
fun PaymentBottomBar(onConfirmClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 20.dp, // shadow-[0_-4px...]
        color = Color.White,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Box(modifier = Modifier.padding(16.dp).navigationBarsPadding()) {
            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Confirm Payment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Surface(
                        color = Color.White.copy(0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "$45.00",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyDeep
                        )
                    }
                }
            }
        }
    }
}
