package com.example.pickleball.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pickleball.R
import com.example.pickleball.data.model.UiState
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
){
    val loginState by authViewModel.loginState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // React to login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Success -> {
                authViewModel.resetLoginState()
                onLoginSuccess()
            }
            is UiState.Error -> {
                errorMessage = (loginState as UiState.Error).message
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundLight)){
        // Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colorStops = arrayOf(
                            0.0f to PrimaryGreen.copy(alpha = 0.25f),
                            0.5f to BackgroundLight,
                            1.0f to BackgroundLight
                        ),
                        center = androidx.compose.ui.geometry.Offset(0f, 0f),
                        radius = 2500f
                    )
                )
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(16.dp).zIndex(2f)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(Modifier.height(48.dp))

            // Logo Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer { rotationZ = 3f}
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(PrimaryGreen, Color(0xFF00D16D))))
                    .shadow(20.dp, RoundedCornerShape(18.dp), ambientColor = PrimaryGreen, spotColor = PrimaryGreen),
                contentAlignment = Alignment.Center
            ){
                Icon(Icons.Default.SportsTennis, null, tint = NavyDeep, modifier = Modifier.size(36.dp))
            }

            Spacer(Modifier.height(20.dp))

            // --- SỬA FONT Ở ĐÂY ---
            Text(
                text = "Pickleball Pro",
                style = MaterialTheme.typography.headlineLarge, // Dùng Lexend Bold
                color = NavyDeep
            )

            Text(
                text = "Welcome back! Log in to access the marketplace.",
                style = MaterialTheme.typography.bodyMedium, // Dùng NotoSans
                color = TextSecondaryLight,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp)
            ){
                Column (modifier = Modifier.padding(20.dp)){
                    // Label
                    Text(
                        text = "Email Address",
                        style = MaterialTheme.typography.titleSmall, // Dùng Lexend Medium
                        color = NavyDeep
                    )

                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Email Address", style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge // Input text dùng NotoSans
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.titleSmall,
                        color = NavyDeep
                    )

                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password", style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondaryLight,
                        modifier = Modifier.align(Alignment.End).clickable { }
                    )

                    Spacer(Modifier.height(20.dp))

                    // Error message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            errorMessage = null
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter email and password"
                            } else {
                                authViewModel.login(email.trim(), password)
                            }
                        },
                        enabled = loginState !is UiState.Loading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep)
                    ) {
                        if (loginState is UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = NavyDeep,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("LOG IN", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Row(verticalAlignment = Alignment.CenterVertically){
                Text("OR CONTINUE WITH", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(horizontal = 12.dp))
                Divider(Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = NavyDeep)
            ) {
                Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "Google", modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Google", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(Modifier.height(28.dp))

            // Footer Text
            val registerText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = NotoSans, color = TextSecondaryLight)) {
                    append("Don't have an account? ")
                }
                pushStringAnnotation(tag = "REGISTER", annotation = "register")
                withStyle(style = SpanStyle(fontFamily = Lexend, fontWeight = FontWeight.Bold, color = PrimaryGreen)){
                    append("Register")
                }
                pop()
            }

            ClickableText(
                text = registerText,
                style = MaterialTheme.typography.bodyMedium,
                onClick = { offset ->
                    registerText.getStringAnnotations(tag = "REGISTER", start = offset, end = offset).firstOrNull()?.let {
                        onRegisterClick()
                    }
                }
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}