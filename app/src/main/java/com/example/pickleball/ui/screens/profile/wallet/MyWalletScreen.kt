package com.example.pickleball.ui.screens.profile.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.theme.*

// --- Data Models ---
data class Transaction(
    val title: String,
    val subtitle: String,
    val amount: String,
    val isPositive: Boolean,
    val icon: ImageVector,
    val dateGroup: String
)

// --- Dummy Data ---
val transactions = listOf(
    Transaction("Match Fee vs. J. Doe", "Court Booking • 2:30 PM", "- $15.00", false, Icons.Default.SportsTennis, "Today"),
    Transaction("Top Up", "Stripe • 10:15 AM", "+ $200.00", true, Icons.Default.AddCard, "Today"),
    Transaction("Pro Carbon Paddle", "Marketplace • 4:45 PM", "- $145.50", false, Icons.Default.ShoppingBag, "Yesterday"),
    Transaction("Courtside Cafe", "Food & Drink • 1:20 PM", "- $8.25", false, Icons.Default.LocalCafe, "Yesterday")
)

@Composable
fun MyWalletScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = { WalletTopBar(onBackClick) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Balance Card
            item { BalanceCard() }

            // 2. Action Buttons
            item { WalletActionButtons(navController) }

            // 3. Filters
            item { TransactionFilters() }

            // 4. Recent Activity List
            item {
                Text(
                    "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
            }

            val grouped = transactions.groupBy { it.dateGroup }
            grouped.forEach { (date, items) ->
                item {
                    Text(
                        date.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep.copy(0.5f),
                        letterSpacing = 1.sp
                    )
                }
                items(items) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}


@Composable
fun WalletTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(CoolGray.copy(0.5f), CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDeep)
        }

        Text(
            text = "My Wallet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BalanceCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.58f)
            .clip(RoundedCornerShape(24.dp))
            .background(NavyDeep)
    ) {
        // Background Effects
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(NavyDeep, Color(0xFF0D1B48), NavyDeep)
                    )
                )
        )
        // Green Glow Top Right
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .background(PrimaryGreen.copy(0.15f), CircleShape)
        )
        // Green Glow Bottom Left
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .background(PrimaryGreen.copy(0.1f), CircleShape)
        )

        // Background Icon Decoration
        Icon(
            imageVector = Icons.Default.SportsTennis,
            contentDescription = null,
            tint = Color.White.copy(0.05f),
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 40.dp)
                .rotate(12f)
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = AccentMint.copy(0.9f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "TOTAL BALANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentMint.copy(0.9f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "$1,250.00",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen,
                        letterSpacing = (-1).sp
                    )
                }
                Icon(Icons.Default.Contactless, null, tint = Color.White.copy(0.5f), modifier = Modifier.size(32.dp))
            }

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    // Chip icon fake
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(28.dp)
                            .background(
                                Brush.linearGradient(listOf(AccentMint, Color.White)),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.Black.copy(0.2f), RoundedCornerShape(2.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "•••• 4289",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White.copy(0.6f),
                        fontSize = 14.sp,
                        letterSpacing = 2.sp
                    )
                }

                // Percentage Badge
                Surface(
                    color = Color.White.copy(0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.White.copy(0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(PrimaryGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowOutward,
                                contentDescription = null,
                                tint = NavyDeep,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+12.5%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WalletActionButtons(navController: NavController) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = { navController.navigate(Routes.ADD_FUNDS)},
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.AddCircle, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Funds", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {navController.navigate(Routes.WITHDRAW_FUNDS)},
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.AccountBalance, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Withdraw", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TransactionFilters() {
    var selectedFilter by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGray, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        listOf("All", "Incoming", "Outgoing").forEachIndexed { index, title ->
            val isSelected = selectedFilter == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .background(
                        if (isSelected) Color.White else Color.Transparent,
                        RoundedCornerShape(6.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { selectedFilter = index },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) NavyDeep else NavyDeep.copy(0.6f)
                )
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGray.copy(0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.size(48.dp),
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(transaction.icon, null, tint = PrimaryGreen, modifier = Modifier.size(24.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Text(transaction.subtitle, style = MaterialTheme.typography.labelSmall, color = NavyDeep.copy(0.6f))
        }

        // Amount
        Text(
            transaction.amount,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isPositive) PrimaryGreenDark else NavyDeep
        )
    }
}