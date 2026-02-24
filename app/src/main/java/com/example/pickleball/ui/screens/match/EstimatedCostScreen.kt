package com.example.pickleball.ui.screens.match

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*

@Composable
fun EstimatedCostScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Scaffold(
        containerColor = WhitePure,
        topBar = {
            EstimatedCostTopBar(onBackClick)
        },
        bottomBar = {
            EstimatedCostBottomBar(onConfirmClick = {navController.navigate(Routes.MATCH_CREATED)})
        }
    ) { paddingValues ->
        // Background Mesh Gradient
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SoftMintLight.copy(0.4f), Color.Transparent),
                            radius = 800f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TotalValueCard()
                FeeBreakdownCard()
                YourShareCard()
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun EstimatedCostTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, RoundedCornerShape(8.dp))
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyDark)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Estimated Cost",
            fontFamily = Lexend,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = NavyDark
        )
    }
}
@Composable
fun TotalValueCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WhitePure),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, CoolGrayLight)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Accent Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(NavyDark)
                    .align(Alignment.CenterHorizontally) // Logic sai ở đây, sửa lại bên dưới
            ) {} // Empty box trick doesn't work well inside Column padding

            Text(
                "TOTAL MATCH VALUE",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = NavyDark.copy(0.4f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Text("$", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = NavyDark, modifier = Modifier.padding(top = 8.dp))
                Text("85", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 64.sp, color = NavyDark, letterSpacing = (-2).sp)
                Text(".00", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = NavyDark, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun FeeBreakdownCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGrayLight, RoundedCornerShape(12.dp))
            .border(1.dp, WhitePure.copy(0.5f), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ReceiptLong, null, tint = NavyDark.copy(0.4f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("FEE BREAKDOWN", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = NavyDark.copy(0.5f), letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Items
        FeeItem("Venue Fee", "$60.00", Icons.Default.Stadium)
        Spacer(modifier = Modifier.height(16.dp))
        FeeItem("Referee Fee", "$20.00", Icons.Default.Sports)
        Spacer(modifier = Modifier.height(16.dp))
        FeeItem("Platform Fee", "$5.00", Icons.Default.Dns)

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = NavyDark.copy(0.1f))
        Spacer(modifier = Modifier.height(12.dp))

        // Subtotal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SUBTOTAL", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = NavyDark.copy(0.4f))
            Text("$85.00", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark.copy(0.6f))
        }
    }
}

@Composable
fun FeeItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = NavyDark, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark)
        }
        Text(value, fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyDark)
    }
}

@Composable
fun YourShareCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = NavyDark.copy(0.1f))
            .background(WhitePure, RoundedCornerShape(12.dp))
            .border(1.dp, CoolGrayLight, RoundedCornerShape(12.dp))
    ) {
        // Decoration Circle (Đẩy hẳn ra góc để không che chữ)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .size(120.dp)
                .background(PrimaryGreen.copy(0.15f), CircleShape)
                .blur(50.dp)
        )

        Column(modifier = Modifier.padding(24.dp)) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Your Share", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NavyDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Split with 3 others", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
                }

                Surface(
                    color = CoolGrayLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CallSplit, null, tint = NavyDark, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SPLIT", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = NavyDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = CoolGrayLight)
            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Row (Total)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("TOTAL TO PAY", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = NavyDark.copy(0.4f))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Taxes included", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.6f))
                }
                Text(
                    "$21.25",
                    fontFamily = Lexend,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = PrimaryGreen,
                    letterSpacing = (-1).sp
                )
            }
        }
    }
}

@Composable
fun EstimatedCostBottomBar(onConfirmClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 16.dp)
    ) {
        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(15.dp, spotColor = PrimaryGreen.copy(0.3f), shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDark)
        ) {
            Text(
                "CONFIRM & PROCEED",
                fontFamily = Lexend,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
        }
    }
}