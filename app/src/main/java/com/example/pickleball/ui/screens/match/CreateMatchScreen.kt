package com.example.pickleball.ui.screens.match

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.*

val PrimaryGreen = Color(0xFF00F684) // Electric Cyan
val NavyDark = Color(0xFF050A30)     // Deep Navy
val CoolGrayLight = Color(0xFFE8EBF0)// Cool Gray
val SoftMintLight = Color(0xFFD6FFF3)// Soft Mint
val WhitePure = Color(0xFFFFFFFF)

@Composable
fun CreateMatchScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    // --- STATE ---
    var selectedFormat by remember { mutableStateOf("Doubles") }
    var paymentMode by remember { mutableStateOf("Split") }
    var isRefereeEnabled by remember { mutableStateOf(false) }
    var skillLevel by remember { mutableFloatStateOf(1350f) } // ELO
    var notes by remember { mutableStateOf("") }

    Scaffold(
        containerColor = WhitePure,
        topBar = {
            CreateMatchTopBar(onBackClick)
        },
        bottomBar = {
            CreateMatchBottomBar(
                onCreate = { navController.navigate(Routes.ESTIMATED_COST) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SectionLabel("LOCATION")
            LocationSelector(navController = navController)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DateTimeInput(
                    label = "DATE",
                    value = "Today, Oct 28",
                    icon = Icons.Default.CalendarToday,
                    modifier = Modifier.weight(1f)
                )
                DateTimeInput(
                    label = "TIME",
                    value = "10:00 AM",
                    icon = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f)
                )
            }
            SectionLabel("FORMAT")
            FormatSelector(
                selected = selectedFormat,
                onSelect = { selectedFormat = it }
            )
            SectionLabel("PAYMENT MODE")
            PaymentModeSelector(
                selected = paymentMode,
                onSelect = { paymentMode = it }
            )

            SectionLabel("EXTRAS")
            RefereeToggle(
                checked = isRefereeEnabled,
                onCheckedChange = { isRefereeEnabled = it }
            )
            SkillLevelSlider(
                value = skillLevel,
                onValueChange = { skillLevel = it }
            )
            SectionLabel("MATCH RULES & NOTES")
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("e.g. Bring your own balls, court 4 is reserved...", color = NavyDark.copy(0.4f), fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(CoolGrayLight, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Transparent),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = WhitePure,
                    unfocusedContainerColor = CoolGrayLight,
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun CreateMatchTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(CoolGrayLight, RoundedCornerShape(8.dp))
                .border(1.dp, WhitePure.copy(0.5f), RoundedCornerShape(8.dp))
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = NavyDark)
        }

        Text(
            text = "Create Casual Match",
            fontFamily = Lexend,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = NavyDark
        )

        Spacer(modifier = Modifier.size(40.dp)) // Dummy spacer to center title
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = NavyDark.copy(0.9f),
        letterSpacing = 0.5.sp
    )
}

@Composable
fun LocationSelector(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGrayLight, RoundedCornerShape(12.dp))
            .border(1.dp, WhitePure.copy(0.6f), RoundedCornerShape(12.dp))
            .clickable { navController.navigate(Routes.FIND_COURT)}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Venice Beach Courts", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyDark)
                Text("1800 Ocean Front Walk", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = NavyDark.copy(0.4f))
    }
}

@Composable
fun DateTimeInput(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SectionLabel(label)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = NavyDark.copy(0.4f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(value, fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark)
        }
    }
}

@Composable
fun FormatSelector(selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        // Singles Option
        FormatOption(
            title = "Singles",
            subtitle = "1 VS 1",
            iconCount = 1,
            isSelected = selected == "Singles",
            onClick = { onSelect("Singles") },
            modifier = Modifier.weight(1f)
        )
        // Doubles Option
        FormatOption(
            title = "Doubles",
            subtitle = "2 VS 2",
            iconCount = 2,
            isSelected = selected == "Doubles",
            onClick = { onSelect("Doubles") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FormatOption(
    title: String,
    subtitle: String,
    iconCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) SoftMintLight.copy(0.3f) else CoolGrayLight
    val borderColor = if (isSelected) PrimaryGreen else Color.Transparent
    val iconColor = if (isSelected) NavyDark else NavyDark.copy(0.6f)

    Box(
        modifier = modifier
            .height(96.dp)
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                repeat(iconCount) {
                    Icon(Icons.Default.Person, null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark)
            Text(subtitle, fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = NavyDark.copy(0.4f))
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp)
                    .background(PrimaryGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = NavyDark, modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun PaymentModeSelector(selected: String, onSelect: (String) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CoolGrayLight, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            // Split Button
            Button(
                onClick = { onSelect("Split") },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "Split") PrimaryGreen else Color.Transparent,
                    contentColor = if (selected == "Split") NavyDark else NavyDark.copy(0.6f)
                ),
                elevation = if(selected == "Split") ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.PieChart, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Split Equally", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Pay All Button
            Button(
                onClick = { onSelect("PayAll") },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == "PayAll") PrimaryGreen else Color.Transparent,
                    contentColor = if (selected == "PayAll") NavyDark else NavyDark.copy(0.6f)
                ),
                elevation = if(selected == "PayAll") ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pay for All", fontFamily = Lexend, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
        Text(
            "Cost is split equally among all participating players.",
            fontFamily = Lexend,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = NavyDark.copy(0.5f),
            modifier = Modifier.padding(top = 6.dp, start = 4.dp)
        )
    }
}

@Composable
fun RefereeToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGrayLight, RoundedCornerShape(12.dp))
            .border(1.dp, WhitePure.copy(0.6f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WhitePure, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsScore, null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Referee", fontFamily = Lexend, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyDark)
                Text("Official tracking (Optional)", fontFamily = Lexend, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = NavyDark.copy(0.5f))
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NavyDark,
                checkedTrackColor = PrimaryGreen,
                uncheckedThumbColor = WhitePure,
                uncheckedTrackColor = NavyDark.copy(0.1f)
            )
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillLevelSlider(value: Float, onValueChange: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoolGrayLight.copy(0.5f), RoundedCornerShape(12.dp))
            .border(1.dp, CoolGrayLight, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Header Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "SKILL LEVEL PREFERENCE",
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyDark.copy(0.9f)
            )
            Surface(color = NavyDark, shape = RoundedCornerShape(4.dp)) {
                Text(
                    "ELO 1200 - 1500",
                    color = WhitePure,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Lexend,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 800f..2500f,
            modifier = Modifier.fillMaxWidth().height(30.dp),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .shadow(6.dp, CircleShape)
                        .background(PrimaryGreen, CircleShape)
                        .border(4.dp, WhitePure, CircleShape)
                )
            },

            track = { sliderState ->
                val fraction = (sliderState.value - sliderState.valueRange.start) /
                        (sliderState.valueRange.endInclusive - sliderState.valueRange.start)


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(CoolGrayLight)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .fillMaxHeight()
                            .background(PrimaryGreen)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("BEGINNER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark.copy(0.4f), fontFamily = Lexend)
            Text("INTERMEDIATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark.copy(0.4f), fontFamily = Lexend)
            Text("PRO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyDark.copy(0.4f), fontFamily = Lexend)
        }
    }
}

@Composable
fun CreateMatchBottomBar(onCreate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhitePure.copy(0.95f))
            .padding(16.dp)
    ) {
        Button(
            onClick = onCreate,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(15.dp, spotColor = PrimaryGreen.copy(0.3f), shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDark)
        ) {
            Icon(Icons.Default.AddCircle, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "CREATE MATCH",
                fontFamily = Lexend,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        }
    }
}