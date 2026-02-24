package com.example.pickleball.ui.screens.profile.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.theme.*

@Composable
fun WithdrawalSuccessScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            WithdrawalSuccessBottomBar(
                onViewHistory = { /* Navigate to history */ },
                onBackToWallet = {
                    // Quay về màn hình Wallet và xóa các màn hình rút tiền khỏi stack
                    navController.navigate(Routes.WALLET) {
                        popUpTo(Routes.WALLET) { inclusive = true }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 1. Success Animation/Icon
            SuccessCheckMark()

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Headings
            Text(
                text = "Withdrawal\nSuccessful!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDeep,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your withdrawal request has been received and is being processed.",
                style = MaterialTheme.typography.bodyMedium,
                color = NavyDeep.copy(0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Receipt Card
            WithdrawalReceiptCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Components ---

@Composable
fun SuccessCheckMark() {
    Box(contentAlignment = Alignment.Center) {
        // Outer Glow (Blurred effect simulation)
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(SoftMint.copy(0.6f), Color.Transparent)
                    )
                )
        )

        // Circle Container
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(SoftMint.copy(0.3f), CircleShape)
                .border(2.dp, SoftMint, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = PrimaryGreen,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun WithdrawalReceiptCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray), // Màu nền xám #E8EBF0
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Amount Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "TOTAL WITHDRAWN",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep.copy(0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$100.00",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyDeep,
                    fontSize = 36.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = NavyDeep.copy(0.1f))
            Spacer(modifier = Modifier.height(20.dp))

            // Details
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ReceiptRow(
                    label = "Sent to",
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                shadowElevation = 1.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.AccountBalance, null, tint = NavyDeep, modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chase Bank •• 4582", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = NavyDeep)
                        }
                    }
                )

                ReceiptRow(
                    label = "Est. Arrival",
                    value = "1-3 Business Days"
                )

                ReceiptRow(
                    label = "Transaction ID",
                    value = "#WD-849201",
                    isMonospace = true
                )
            }
        }
    }
}

@Composable
fun ReceiptRow(
    label: String,
    value: String? = null,
    content: @Composable (() -> Unit)? = null,
    isMonospace: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.7f))
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isMonospace) FontWeight.Normal else FontWeight.Bold,
                color = if (isMonospace) NavyDeep.copy(0.8f) else NavyDeep,
                fontFamily = if (isMonospace) FontFamily.Monospace else null
            )
        } else {
            content?.invoke()
        }
    }
}

@Composable
fun WithdrawalSuccessBottomBar(
    onViewHistory: () -> Unit,
    onBackToWallet: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("View Transaction History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onBackToWallet,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Wallet", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = NavyDeep)
            }
        }
    }
}