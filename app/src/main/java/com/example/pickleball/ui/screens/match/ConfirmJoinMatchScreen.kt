package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ConfirmJoinMatchScreen(
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MatchTheme.White,
        topBar = {
            ConfirmTopBar(onBackClick)
        },
        bottomBar = {
            ConfirmBottomBar(
                onConfirm = onConfirmClick,
                onCancel = onCancelClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { MatchSummaryCard() }
            item { PaymentDetailsSection() }
            item { RulesAndPenaltyCard() }
            item { PaymentMethodSelector() }
        }
    }
}

// --- SUB-COMPOSABLES ---

@Composable
fun ConfirmTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MatchTheme.White.copy(alpha = 0.95f))
            .border(width = 1.dp, color = MatchTheme.CoolGray.copy(alpha = 0.5f), shape = androidx.compose.ui.graphics.RectangleShape) // Border bottom fake
            .padding(horizontal = 8.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MatchTheme.Navy
            )
        }

        Text(
            text = "Confirm Join Match",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MatchTheme.Navy,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MatchSummaryCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MatchTheme.CoolGray)
            .border(1.dp, MatchTheme.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        // Mesh Gradient Background Effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MatchTheme.SoftMint.copy(alpha = 0.6f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(x = 100f, y = 100f),
                        radius = 600f
                    )
                )
        )

        Column(modifier = Modifier.padding(20.dp)) {
            // Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    color = MatchTheme.Primary,
                    shape = RoundedCornerShape(50),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = "RANKED MATCH",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MatchTheme.Navy,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MatchTheme.Navy.copy(0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "OPEN SLOT",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MatchTheme.Navy.copy(0.7f),
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time & Date
            Text(
                text = "10:00 AM",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MatchTheme.Navy,
                fontSize = 32.sp
            )
            Text(
                text = "Saturday, Oct 24 • 90 min",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MatchTheme.Navy.copy(0.8f),
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Info Rows
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MatchInfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "COURT",
                    value = "Venice Beach Courts, Court 4"
                )
                MatchInfoRow(
                    icon = Icons.Default.SportsBaseball,
                    label = "LEVEL",
                    value = "Advanced (4.0+)"
                )
            }
        }
    }
}

@Composable
fun MatchInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MatchTheme.White,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = MatchTheme.Navy, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy.copy(0.6f))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
        }
    }
}

@Composable
fun PaymentDetailsSection() {
    Column {
        Text(
            text = "PAYMENT DETAILS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MatchTheme.Navy,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp),
            letterSpacing = 1.sp
        )

        Surface(
            color = MatchTheme.CoolGray,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MatchTheme.White.copy(0.5f)),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                PaymentRowItem("Match Entry Fee", "$12.00")
                Spacer(modifier = Modifier.height(12.dp))
                PaymentRowItem("Court Reservation Deposit", "$3.00")

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MatchTheme.Navy.copy(0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Due", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
                    Text("$15.00", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MatchTheme.Navy)
                }
            }
        }
    }
}

@Composable
fun PaymentRowItem(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MatchTheme.Navy.copy(0.7f))
        Text(amount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MatchTheme.Navy)
    }
}

@Composable
fun RulesAndPenaltyCard() {
    Surface(
        color = MatchTheme.SoftMint.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MatchTheme.SoftMint),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = MatchTheme.Navy,
                modifier = Modifier.size(20.dp).offset(y = 2.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "MATCH RULES & PENALTY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MatchTheme.Navy,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Rich Text for Bold parts
                val annotatedString = buildAnnotatedString {
                    append("Cancellations made less than 2 hours before match time will result in a ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$5.00 penalty")
                    }
                    append(". Please arrive 10 minutes early. No-shows are recorded and affect your reliability score.")
                }

                Text(
                    text = annotatedString,
                    fontSize = 12.sp,
                    color = MatchTheme.Navy.copy(0.8f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun PaymentMethodSelector() {
    Surface(
        color = MatchTheme.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MatchTheme.CoolGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Fake Visa Icon
                Surface(
                    color = MatchTheme.Navy,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.size(width = 36.dp, height = 24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("VISA", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("•••• 4242", fontWeight = FontWeight.Bold, color = MatchTheme.Navy, fontSize = 14.sp)
            }

            TextButton(onClick = {}) {
                Text("CHANGE", fontWeight = FontWeight.Bold, color = MatchTheme.Primary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ConfirmBottomBar(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        color = MatchTheme.White.copy(0.95f),
        shadowElevation = 16.dp,
        border = BorderStroke(1.dp, MatchTheme.CoolGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 16.dp) // SafeArea
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MatchTheme.Primary,
                    contentColor = MatchTheme.Navy
                ),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Confirm Join & Pay", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MatchTheme.Navy)
            ) {
                Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
