package com.example.pickleball.ui.screens.profile.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoveUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.screens.booking.SoftMint
import com.example.pickleball.ui.theme.*

@Composable
fun WithdrawFundsScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    // State quản lý số tiền và phương thức rút
    var selectedAmount by remember { mutableStateOf<Int?>(100) } // Mặc định chọn $100
    var customAmount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("Bank") }

    val withdrawalAmount = if (selectedAmount != null) {
        String.format("%.2f", selectedAmount!!.toDouble())
    } else if (customAmount.isNotEmpty()) {
        customAmount
    } else {
        "0.00"
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            WithdrawTopBar(onBackClick)
        },
        bottomBar = {
            WithdrawBottomBar(
                amount = withdrawalAmount,
                onConfirmClick = { navController.navigate(Routes.CONFIRM_WITHDRAWAL) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Available Balance Card
            AvailableBalanceCard()

            // 2. Withdraw Amount Section
            WithdrawAmountSection(
                selectedAmount = selectedAmount,
                customAmount = customAmount,
                onAmountSelected = { amount ->
                    selectedAmount = amount // Nếu là "Max" (Int.MAX_VALUE), xử lý logic riêng
                    customAmount = ""
                },
                onCustomAmountChanged = { input ->
                    customAmount = input
                    selectedAmount = null
                }
            )

            // 3. Withdraw To Section
            WithdrawMethodSection(
                selectedMethod = selectedMethod,
                onMethodSelected = { selectedMethod = it }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Components ---

@Composable
fun WithdrawTopBar(onBackClick: () -> Unit) {
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
            text = "Withdraw Funds",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AvailableBalanceCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = NavyDeep.copy(0.2f))
            .clip(RoundedCornerShape(24.dp))
            .background(NavyDeep)
    ) {
        // Decorative Blurs
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(PrimaryGreen.copy(0.1f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
                .background(SoftMint.copy(0.05f), CircleShape)
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
                Text(
                    "AVAILABLE BALANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.7f),
                    letterSpacing = 1.sp
                )

                // Verified Badge
                Surface(
                    color = Color.White.copy(0.1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.White.copy(0.05f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, null, tint = PrimaryGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verified", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // Balance
            Text(
                "$1,250.00",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreen,
                letterSpacing = (-1).sp
            )

            // Footer
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).background(PrimaryGreen, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "DIGITAL WALLET",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.4f),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun WithdrawAmountSection(
    selectedAmount: Int?,
    customAmount: String,
    onAmountSelected: (Int) -> Unit,
    onCustomAmountChanged: (String) -> Unit
) {
    Column {
        Text(
            "WITHDRAW AMOUNT",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.8f),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Preset Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WithdrawAmountButton(text = "$50", isSelected = selectedAmount == 50, onClick = { onAmountSelected(50) }, modifier = Modifier.weight(1f))
            WithdrawAmountButton(text = "$100", isSelected = selectedAmount == 100, onClick = { onAmountSelected(100) }, modifier = Modifier.weight(1f))
            WithdrawAmountButton(text = "Max", isSelected = selectedAmount == 1250, onClick = { onAmountSelected(1250) }, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Input
        Column {
            Text(
                "Custom Amount",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = NavyDeep.copy(0.6f),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = customAmount,
                onValueChange = onCustomAmountChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(CoolGray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("$", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box {
                            if (customAmount.isEmpty() && selectedAmount == null) {
                                Text("0.00", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDeep.copy(0.4f))
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun WithdrawAmountButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bgColor = if (isSelected) PrimaryGreen else CoolGray
    val shadow = if (isSelected) 8.dp else 0.dp
    val border = if (isSelected) BorderStroke(2.dp, Color.White.copy(0.5f)) else null

    Button(
        onClick = onClick,
        modifier = modifier
            .height(40.dp)
            .shadow(shadow, RoundedCornerShape(50), spotColor = PrimaryGreen.copy(0.4f)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor, contentColor = NavyDeep),
        border = border,
        elevation = ButtonDefaults.buttonElevation(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun WithdrawMethodSection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {
    Column {
        Text(
            "WITHDRAW TO",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.8f),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Bank Account
            WithdrawMethodItem(
                title = "Bank Account",
                subtitle = "1-3 Business Days • Free",
                icon = Icons.Default.AccountBalance,
                isSelected = selectedMethod == "Bank",
                onClick = { onMethodSelected("Bank") }
            )

            // PayPal
            WithdrawMethodItem(
                title = "PayPal",
                subtitle = "Instant • 1.5% Fee",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = selectedMethod == "PayPal",
                onClick = { onMethodSelected("PayPal") }
            )

            // Add New Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { },
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, NavyDeep.copy(0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.AddCircle, null, tint = NavyDeep.copy(0.8f), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Withdrawal Method", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDeep.copy(0.8f))
                }
            }
        }
    }
}

@Composable
fun WithdrawMethodItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color.White else CoolGray
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val ringColor = if (isSelected) PrimaryGreen.copy(0.2f) else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(2.dp, ringColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = CircleShape,
                color = if(isSelected) CoolGray.copy(0.5f) else Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = NavyDeep)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDeep)
                Text(subtitle, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NavyDeep.copy(0.6f))
            }

            // Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) PrimaryGreen else Color.Transparent)
                    .border(if (isSelected) 0.dp else 2.dp, if (isSelected) Color.Transparent else NavyDeep.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(Icons.Default.Check, null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun WithdrawBottomBar(amount: String, onConfirmClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, CoolGray)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Withdrawal Amount", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = NavyDeep.copy(0.7f))
                Text("$$amount", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = NavyDeep)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.MoveUp, null) // Hoặc icon khác
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm & Withdraw", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}