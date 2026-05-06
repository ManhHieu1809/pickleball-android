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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pickleball.ui.screens.booking.CoolGray
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.WalletViewModel

@Composable
fun AddFundsScreen(
    navController: NavController,
    viewModel: WalletViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var selectedAmount by remember { mutableStateOf<Int?>(50) }
    var customAmount by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Visa") }

    val walletBalance by viewModel.walletBalance.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWalletData()
    }

    val totalToPay = if (selectedAmount != null) {
        String.format("%.2f", selectedAmount!!.toDouble())
    } else if (customAmount.isNotEmpty()) {
        customAmount
    } else {
        "0.00"
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            AddFundsTopBar(onBackClick)
        },
        bottomBar = {
            AddFundsBottomBar(
                totalToPay = totalToPay,
                isLoading = isLoading,
                onConfirm = {
                    val amount = selectedAmount?.toDouble() ?: customAmount.toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        viewModel.topUp(amount)
                        navController.popBackStack()
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
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Wallet Info Card
            WalletInfoCard(walletBalance?.balance ?: 0.0)

            // 2. Select Amount Section
            SelectAmountSection(
                selectedAmount = selectedAmount,
                customAmount = customAmount,
                onAmountSelected = { amount ->
                    selectedAmount = amount
                    customAmount = "" // Reset custom input
                },
                onCustomAmountChanged = { input ->
                    customAmount = input
                    selectedAmount = null // Deselect presets
                }
            )

            // 3. Payment Method Section
            PaymentMethodSelection(
                selectedMethod = selectedPaymentMethod,
                onMethodSelected = { selectedPaymentMethod = it }
            )

            // Spacer để nội dung không bị che bởi BottomBar
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Components ---

@Composable
fun AddFundsTopBar(onBackClick: () -> Unit) {
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
            text = "Add Funds",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = NavyDeep,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun WalletInfoCard(balance: Double = 0.0) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = NavyDeep.copy(0.2f))
            .clip(RoundedCornerShape(24.dp))
            .background(NavyDeep)
    ) {
        // Background Decorations (Blur circles)
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(PrimaryGreen.copy(0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
                .background(PrimaryGreen.copy(0.05f), CircleShape)
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
                    Text(
                        "CURRENT BALANCE",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        java.text.NumberFormat.getCurrencyInstance(java.util.Locale("vi", "VN")).format(balance),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen,
                        letterSpacing = (-1).sp
                    )
                }

                // Wallet Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(0.1f), CircleShape)
                        .border(1.dp, Color.White.copy(0.05f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(PrimaryGreen, CircleShape)
                            .shadow(8.dp, CircleShape, spotColor = PrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "ACTIVE WALLET",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    "•••• 8829",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = Color.White.copy(0.3f),
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun SelectAmountSection(
    selectedAmount: Int?,
    customAmount: String,
    onAmountSelected: (Int) -> Unit,
    onCustomAmountChanged: (String) -> Unit
) {
    Column {
        Text(
            "SELECT AMOUNT",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.8f),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Preset Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AmountButton(amount = 25, isSelected = selectedAmount == 25, onClick = { onAmountSelected(25) }, modifier = Modifier.weight(1f))
            AmountButton(amount = 50, isSelected = selectedAmount == 50, onClick = { onAmountSelected(50) }, modifier = Modifier.weight(1f))
            AmountButton(amount = 100, isSelected = selectedAmount == 100, onClick = { onAmountSelected(100) }, modifier = Modifier.weight(1f))
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
                            if (customAmount.isEmpty()) {
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
fun AmountButton(amount: Int, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bgColor = if (isSelected) PrimaryGreen else CoolGray
    val shadow = if (isSelected) 8.dp else 0.dp
    val border = if (isSelected) BorderStroke(2.dp, Color.White.copy(0.5f)) else null // Hiệu ứng ring

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .shadow(shadow, RoundedCornerShape(50), spotColor = PrimaryGreen.copy(0.4f)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor, contentColor = NavyDeep),
        border = border,
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text("$$amount", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun PaymentMethodSelection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {
    Column {
        Text(
            "PAYMENT METHOD",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = NavyDeep.copy(0.8f),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Visa Option
            PaymentMethodItem(
                title = "Visa •••• 4242",
                subtitle = "Expires 12/26",
                icon = Icons.Default.CreditCard,
                isSelected = selectedMethod == "Visa",
                onClick = { onMethodSelected("Visa") }
            )

            // Apple Pay Option
            PaymentMethodItem(
                title = "Apple Pay",
                subtitle = "Linked to Wallet",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = selectedMethod == "ApplePay",
                onClick = { onMethodSelected("ApplePay") }
            )

            // Add New Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { },
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, NavyDeep.copy(0.3f)) // Dashed border fake bằng solid nhạt
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.AddCircle, null, tint = NavyDeep.copy(0.8f), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add New Payment Method", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDeep.copy(0.8f))
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color.White else CoolGray
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val ringColor = if (isSelected) PrimaryGreen.copy(0.2f) else Color.Transparent // Giả lập ring focus

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(2.dp, ringColor, RoundedCornerShape(12.dp)), // Ring effect
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor), // Viền chính
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                shadowElevation = 1.dp,
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

            // Checkbox/Radio
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
fun AddFundsBottomBar(totalToPay: String, isLoading: Boolean, onConfirm: () -> Unit) {
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
                Text("Total to pay", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = NavyDeep.copy(0.7f))
                Text("$$totalToPay", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = NavyDeep)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirm,
                enabled = !isLoading && totalToPay != "0.00",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = NavyDeep, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Payments, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm & Add Funds", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}