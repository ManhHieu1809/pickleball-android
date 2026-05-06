package com.example.pickleball.ui.screens.referee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import com.example.pickleball.data.model.UiState
import com.example.pickleball.viewmodel.RefereeViewModel
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val DisputeSurfaceBackground = Color(0xFFF7F9FF)
val DisputeSurfaceCard = Color(0xFFFFFFFF)
val DisputeSurfaceLow = Color(0xFFF1F4F9)
val DisputePrimaryDarkText = Color(0xFF050A30)
val DisputeTextSecondary = Color(0xFF64748B)
val DisputePrimaryNeon = Color(0xFF00F684)

val ErrorContainer = Color(0xFFFFDAD6)
val ErrorColor = Color(0xFFBA1A1A)
val UploadBg = Color(0xFFC0E8DD).copy(alpha = 0.2f)
val UploadBorder = Color(0xFFC0E8DD)
val UploadIconBg = Color(0xFFC0E8DD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisputeCenterScreen(
    navController: NavController,
    viewModel: RefereeViewModel,
    disputeId: Long
) {
    val disputesState by viewModel.disputesState.collectAsState()
    val submitState by viewModel.submitEvidenceState.collectAsState()
    var evidenceUrl by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(disputeId) {
        viewModel.loadDisputes()
    }

    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) {
            viewModel.resetSubmitEvidenceState()
            navController.popBackStack()
        }
    }

    val dispute = (disputesState as? UiState.Success)?.data?.firstOrNull { it.id == disputeId }
    val historyState by viewModel.historyMatchesState.collectAsState()
    val match = (historyState as? UiState.Success)?.data?.firstOrNull { it.rankedMatchId == dispute?.rankedMatchId }

    val players = match?.playerCandidates ?: emptyList()
    val team1Name = if (players.isNotEmpty()) {
        val half = (players.size + 1) / 2
        players.take(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
    } else "Team A"

    val team2Name = if (players.size > 1) {
        val half = (players.size + 1) / 2
        players.drop(half).mapNotNull { it.fullName?.split(" ")?.lastOrNull() ?: it.fullName }.joinToString(" / ")
    } else "Team B"

    Scaffold(
        containerColor = DisputeSurfaceBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "DISPUTE CENTER",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = DisputePrimaryDarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = DisputePrimaryDarkText)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "More", tint = DisputePrimaryDarkText)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DisputeSurfaceBackground)
            )
        },
        bottomBar = {
            DashboardBottomNavigation(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WarningBanner()
            CountdownCard(createdAt = dispute?.createdAt)
            MatchDetailsCard(
                courtText = "Match #${dispute?.rankedMatchId ?: "-"}",
                scoreText = dispute?.status ?: "PENDING",
                team1Name = team1Name,
                team2Name = team2Name
            )
            EvidenceSection(
                evidenceUrl = evidenceUrl,
                notes = notes,
                onEvidenceUrlChange = { evidenceUrl = it },
                onNotesChange = { notes = it }
            )
            SubmitButton(
                enabled = evidenceUrl.isNotBlank() && notes.isNotBlank() && submitState !is UiState.Loading,
                isSubmitting = submitState is UiState.Loading,
                onSubmit = {
                    viewModel.submitEvidence(
                        disputeId = disputeId,
                        evidenceUrl = evidenceUrl,
                        responseText = notes
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp)) // Safe space for bottom nav
        }
    }
}

@Composable
fun WarningBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ErrorContainer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Warning, contentDescription = "Warning", tint = ErrorColor)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Result Disputed by Player",
            fontWeight = FontWeight.Bold,
            color = ErrorColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CountdownCard(createdAt: String?) {
    var timeRemaining by remember { mutableStateOf("23:59:59") }

    LaunchedEffect(createdAt) {
        if (createdAt == null) {
            timeRemaining = "23:59:59"
            return@LaunchedEffect
        }
        
        try {
            val createdTime = ZonedDateTime.parse(createdAt).toInstant()
            val deadline = createdTime.plus(Duration.ofHours(24))
            
            while(true) {
                val now = Instant.now()
                val duration = Duration.between(now, deadline)
                
                if (duration.isNegative || duration.isZero) {
                    timeRemaining = "00:00:00"
                    break
                }
                
                val hours = duration.toHours()
                val mins = duration.toMinutesPart()
                val secs = duration.toSecondsPart()
                
                timeRemaining = String.format("%02d:%02d:%02d", hours, mins, secs)
                delay(1000)
            }
        } catch (e: Exception) {
            // Fallback if parsing fails
            timeRemaining = "23:59:59"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DisputeSurfaceCard)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TIME REMAINING TO RESPOND",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DisputeTextSecondary,
            letterSpacing = 1.sp
        )

        // Timer Text
        Text(
            text = timeRemaining,
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = ErrorColor,
            letterSpacing = (-1).sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Text(
            text = "Failure to provide evidence will result in automatic ruling for the disputing party.",
            fontSize = 14.sp,
            color = DisputeTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun MatchDetailsCard(courtText: String, scoreText: String, team1Name: String, team2Name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DisputeSurfaceLow)
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Match Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DisputePrimaryDarkText)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0E3E8))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = courtText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DisputePrimaryDarkText)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E3E8))

        // Teams & Score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamAvatar(name = team1Name)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "FINAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DisputeTextSecondary, letterSpacing = 1.sp)
                Text(
                    text = scoreText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = DisputePrimaryDarkText
                )
            }

            TeamAvatar(name = team2Name)
        }
    }
}

@Composable
fun TeamAvatar(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(2.dp, DisputeSurfaceCard, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Person, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DisputePrimaryDarkText)
    }
}

@Composable
fun EvidenceSection(
    evidenceUrl: String,
    notes: String,
    onEvidenceUrlChange: (String) -> Unit,
    onNotesChange: (String) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onEvidenceUrlChange(uri.toString())
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Provide Evidence", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DisputePrimaryDarkText)

        // Upload Area (Dashed Border)
        val stroke = Stroke(
            width = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(color = UploadBorder, style = stroke, cornerRadius = CornerRadius(16.dp.toPx()))
                }
                .clip(RoundedCornerShape(16.dp))
                .background(UploadBg)
                .clickable {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(UploadIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.AddAPhoto, contentDescription = "Upload", tint = Color(0xFF284D45), modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Upload Photo/Video Evidence", fontWeight = FontWeight.Bold, color = DisputePrimaryDarkText, fontSize = 14.sp)
            Text(text = "JPG, PNG or MP4 up to 50MB", color = DisputeTextSecondary, fontSize = 12.sp)
        }

        OutlinedTextField(
            value = evidenceUrl,
            onValueChange = onEvidenceUrlChange,
            label = { Text("Evidence Folder URL") },
            placeholder = { Text("https://...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        // Explanation Text Field
        Column {
            Text(text = "Explanation Notes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DisputeTextSecondary, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
            TextField(
                value = notes,
                onValueChange = onNotesChange,
                placeholder = { Text("Describe what happened...", color = DisputeTextSecondary.copy(alpha = 0.5f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DisputeSurfaceLow,
                    unfocusedContainerColor = DisputeSurfaceLow,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun SubmitButton(
    enabled: Boolean,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF006D37), DisputePrimaryNeon)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(50))
            .background(if (enabled) gradientBrush else Brush.horizontalGradient(listOf(DisputeTextSecondary, DisputeTextSecondary)))
            .clickable(enabled = enabled) { onSubmit() }
            .shadow(8.dp, RoundedCornerShape(50), ambientColor = DisputePrimaryNeon),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = DisputePrimaryDarkText, strokeWidth = 2.dp)
            } else {
                Text(
                    text = "SUBMIT EVIDENCE TO ADMIN",
                    color = DisputePrimaryDarkText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Rounded.Send, contentDescription = null, tint = DisputePrimaryDarkText)
            }
        }
    }
}

