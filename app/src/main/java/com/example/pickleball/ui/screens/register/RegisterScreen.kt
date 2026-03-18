package com.example.pickleball.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pickleball.data.model.UiState
import com.example.pickleball.ui.theme.*
import com.example.pickleball.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val registerState by authViewModel.registerState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val passwordError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword
    val emailError = email.isNotEmpty() && !isValidEmail(email)

    LaunchedEffect(registerState) {
        when (registerState) {
            is UiState.Success -> {
                authViewModel.resetRegisterState()
                onRegisterSuccess()
            }
            is UiState.Error -> {
                errorMessage = (registerState as UiState.Error).message
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colorStops = arrayOf(0.0f to PrimaryGreen.copy(0.25f), 1.0f to BackgroundLight),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = 3000f
                )
            )
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(16.dp).zIndex(1f)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(56.dp))

            Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = NavyDeep)

            Text(
                "Create your player account and join the ultimate Pickleball community.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryLight,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            LabeledField("Full Name") {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Jane Doe", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge, // Input font
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                )
            }

            LabeledField("Email Address") {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("jane@example.com", style = MaterialTheme.typography.bodyMedium) },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
                )
            }
            if (emailError) Text("Invalid email address.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

            LabeledField("Password") {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", style = MaterialTheme.typography.bodyMedium) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
                )
            }

            LabeledField("Confirm Password") {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("••••••••", style = MaterialTheme.typography.bodyMedium) },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError,
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                )
            }
            if (passwordError) Text("Passwords do not match.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(16.dp))

            val annotatedText = buildAnnotatedString {
                append("I agree to the ")
                withStyle(style = SpanStyle(fontFamily = Lexend, fontWeight = FontWeight.Bold, color = NavyDeep)) { append("Terms of Service") }
                append(" and ")
                withStyle(style = SpanStyle(fontFamily = Lexend, fontWeight = FontWeight.Bold, color = NavyDeep)) { append("Privacy Policy") }
                append(".")
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = acceptTerms, onCheckedChange = { acceptTerms = it })
                Text(text = annotatedText, style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
            }

            Spacer(Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    errorMessage = null
                    authViewModel.register(email.trim(), password, fullName.trim(), phone.trim())
                },
                enabled = fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && !passwordError && acceptTerms && !emailError && registerState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = NavyDeep),
            ) {
                if (registerState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = NavyDeep, strokeWidth = 2.dp)
                } else {
                    Text("REGISTER NOW", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, style = MaterialTheme.typography.titleSmall, color = NavyDeep, modifier = Modifier.padding(bottom = 6.dp))
        content()
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}