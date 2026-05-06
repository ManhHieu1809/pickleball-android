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
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pickleball.data.model.RefereeQuestionDTO
import com.example.pickleball.data.model.UiState
import com.example.pickleball.navigation.Routes
import com.example.pickleball.ui.theme.PrimaryGreen
import com.example.pickleball.ui.theme.TextSecondaryLight
import com.example.pickleball.viewmodel.RefereeViewModel

val QuizSurfaceBackground = Color(0xFFF7F9FF)
val QuizSurfaceCard = Color(0xFFFFFFFF)
val QuizPrimaryDarkText = Color(0xFF050A30)
val QuizOutlineColor = Color(0xFFB9CBBA)
val QuizSuccessBg = Color(0xFFD6FFF3)
val QuizPrimaryNeon = PrimaryGreen
val QuizSurfaceLow = Color(0xFFF1F4F9)
val QuizTextSecondary = TextSecondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AICertificationQuizScreen(
    navController: NavController,
    viewModel: RefereeViewModel
) {
    val quizState by viewModel.quizState.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()
    
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf(mapOf<Long, String>()) }

    LaunchedEffect(Unit) {
        viewModel.generateQuiz()
    }

    LaunchedEffect(submissionState) {
        if (submissionState is UiState.Success) {
            navController.navigate(Routes.CERTIFICATION_RESULT) {
                popUpTo(Routes.REFEREE_HUB) { inclusive = false }
            }
        }
    }

    Scaffold(
        containerColor = QuizSurfaceBackground,
        topBar = {
            val progress = when (quizState) {
                is UiState.Success -> {
                    val questions = (quizState as UiState.Success).data
                    if (questions.isNotEmpty()) (currentQuestionIndex + 1).toFloat() / questions.size else 0f
                }
                else -> 0f
            }
            Column {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = QuizPrimaryNeon,
                    trackColor = QuizSurfaceLow
                )
                QuizTopBar(onClose = { navController.popBackStack() })
            }
        },
        bottomBar = {
            QuizBottomNavigation(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = quizState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = QuizPrimaryNeon)
                }
                is UiState.Error -> {
                    Text(state.message, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
                is UiState.Success -> {
                    val questions = state.data
                    if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
                        val currentQuestion = questions[currentQuestionIndex]
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            QuestionSection(
                                question = currentQuestion,
                                currentIndex = currentQuestionIndex,
                                totalCount = questions.size,
                                selectedAnswer = userAnswers[currentQuestion.id],
                                onAnswerSelected = { answer ->
                                    userAnswers = userAnswers.toMutableMap().apply { put(currentQuestion.id, answer) }
                                }
                            )
                            
                            AIExplanationSection(
                                isAnswered = userAnswers.containsKey(currentQuestion.id),
                                isLastQuestion = currentQuestionIndex == questions.size - 1,
                                onNext = {
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                    } else {
                                        viewModel.submitQuiz(userAnswers)
                                    }
                                },
                                isSubmitting = submissionState is UiState.Loading
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun QuizTopBar(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(QuizSurfaceBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Close & Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClose) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = QuizTextSecondary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AI Certification",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = QuizPrimaryDarkText
            )
        }

        // Right side: Timer & Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Time Remaining", fontSize = 10.sp, color = QuizTextSecondary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Timer, contentDescription = null, modifier = Modifier.size(14.dp), tint = QuizPrimaryDarkText)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "04:59", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = QuizPrimaryDarkText)
                }
            }
            // Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(QuizSurfaceLow)
                    .border(2.dp, QuizSurfaceLow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = "Avatar", tint = QuizTextSecondary)
            }
        }
    }
}

@Composable
fun QuestionSection(
    question: RefereeQuestionDTO,
    currentIndex: Int,
    totalCount: Int,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(QuizSurfaceCard)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QUESTION ${currentIndex + 1} OF $totalCount",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = QuizTextSecondary,
                letterSpacing = 1.sp
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(QuizSurfaceLow)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = "Scenario: ${question.category ?: "General"}", fontSize = 10.sp, color = QuizTextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question
        Text(
            text = question.questionText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = QuizPrimaryDarkText,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Answers
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AnswerOption(
                text = question.optionA,
                isSelected = selectedAnswer == "A",
                onClick = { onAnswerSelected("A") }
            )
            AnswerOption(
                text = question.optionB,
                isSelected = selectedAnswer == "B",
                onClick = { onAnswerSelected("B") }
            )
            AnswerOption(
                text = question.optionC,
                isSelected = selectedAnswer == "C",
                onClick = { onAnswerSelected("C") }
            )
            AnswerOption(
                text = question.optionD,
                isSelected = selectedAnswer == "D",
                onClick = { onAnswerSelected("D") }
            )
        }
    }
}

@Composable
fun AnswerOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) QuizSuccessBg else QuizSurfaceCard
    val borderColor = if (isSelected) QuizPrimaryNeon else QuizOutlineColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Radio/Check Icon
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) QuizPrimaryNeon else Color.Transparent)
                .border(
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else QuizOutlineColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = QuizPrimaryDarkText, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) QuizPrimaryDarkText else QuizTextSecondary,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun AIExplanationSection(
    isAnswered: Boolean,
    isLastQuestion: Boolean,
    onNext: () -> Unit,
    isSubmitting: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(QuizSurfaceCard)
            .border(1.dp, QuizOutlineColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(QuizSurfaceLow.copy(alpha = 0.5f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Psychology, contentDescription = null, tint = Color(0xFF006D37))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AI Hint", fontWeight = FontWeight.Bold, color = QuizPrimaryDarkText)
            }
        }

        HorizontalDivider(color = QuizOutlineColor.copy(alpha = 0.2f))

        // Content
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Focus on the USAPA rulebook section regarding court lines and net serves. Remember that in Pickleball, all lines except the kitchen line are considered 'in'.",
                fontSize = 14.sp,
                color = QuizTextSecondary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = QuizOutlineColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            Button(
                onClick = onNext,
                enabled = isAnswered && !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = QuizPrimaryNeon,
                    disabledContainerColor = QuizSurfaceLow
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = QuizPrimaryDarkText, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = if (isLastQuestion) "Submit Quiz" else "Next Question",
                        color = if (isAnswered) QuizPrimaryDarkText else QuizTextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuizBottomNavigation(navController: NavController) {
    NavigationBar(
        containerColor = QuizSurfaceCard,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { navController.navigate(Routes.HOME) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = "Rules") },
            label = { Text("Rules", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(QuizPrimaryNeon)
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Rounded.AssignmentTurnedIn, contentDescription = "Exams", tint = QuizPrimaryDarkText)
                }
            },
            label = { Text("Exams", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = true,
            onClick = { /* Already here or same module */ },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedTextColor = QuizPrimaryDarkText
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            selected = false,
            onClick = { navController.navigate(Routes.PROFILE) }
        )
    }
}
