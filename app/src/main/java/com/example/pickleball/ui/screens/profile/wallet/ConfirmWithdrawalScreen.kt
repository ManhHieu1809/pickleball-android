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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.theme.*

@Composable
fun ConfirmWithdrawalScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            ConfirmWithdrawalTopBar(onBackClick)
        },
        bottomBar = {
            ConfirmWithdrawalBottomBar(
                onConfirm = onConfirmClick,
                onCancel = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Amount Display Card
            WithdrawalAmountCard()

            // 2. Transaction Details
            TransactionDetailsCard()

            // 3. Security Note
            SecurityNote()

            // Spacer for bottom bar
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Components ---

@Composable
fun ConfirmWithdrawalTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 16.dp, horizontal = 16.dp)
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
            text = "Confirm Withdrawal",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun WithdrawalAmountCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = NavyDeep.copy(0.2f))
            .clip(RoundedCornerShape(20.dp))
            .background(NavyDeep),
        contentAlignment = Alignment.Center
    ) {
        // Background Decorative Glows
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = (-30).dp)
                .background(PrimaryGreen.copy(0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
                .background(SoftMint.copy(0.05f), CircleShape)
        )

        // Content
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "AMOUNT TO WITHDRAW",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(0.7f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$100.00",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreen,
                letterSpacing = (-1).sp
            )
        }
    }
}

@Composable
fun TransactionDetailsCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoolGray),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "TRANSACTION DETAILS",
                style = MaterialTheme.typography.labelSmall,
                color = NavyDeep.copy(0.6f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )

            // Bank Account Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bank Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SoftMint, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AccountBalance, null, tint = NavyDeep, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text
                Column(modifier = Modifier.weight(1f)) {
                    Text("Chase Bank", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("Checking •••• 4582", style = MaterialTheme.typography.bodySmall, color = NavyDeep.copy(0.6f))
                }

                // Check Icon
                Icon(Icons.Default.CheckCircle, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details List
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                DetailRow("Processing Time", "1-3 Business Days")
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Service Fee", "Free")

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = NavyDeep.copy(0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total to Receive", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("$100.00", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.7f))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = NavyDeep)
    }
}

@Composable
fun SecurityNote() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftMint.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(1.dp, SoftMint, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = NavyDeep,
            modifier = Modifier.size(20.dp).offset(y = 1.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Secure Transaction", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NavyDeep)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your funds will be securely transferred to your verified bank account. Please review all details before confirming.",
                style = MaterialTheme.typography.bodySmall,
                color = NavyDeep.copy(0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ConfirmWithdrawalBottomBar(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Confirm Withdrawal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge, color = NavyDeep)
            }
        }
    }
}