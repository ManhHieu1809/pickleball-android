package com.example.pickleball.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pickleball.R
import com.example.pickleball.ui.components.PrimaryButton
import com.example.pickleball.ui.theme.NavyDeep
import com.example.pickleball.ui.theme.NotoSans
import com.example.pickleball.ui.theme.PickleballTheme
import com.example.pickleball.ui.theme.PrimaryGreen
import com.example.pickleball.ui.theme.TextSecondaryLight

@Composable
fun OnboardingScreens(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(R.drawable.onboarding_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp)
                .zIndex(2f),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .graphicsLayer {
                        rotationZ = 3f
                    }
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryGreen),

                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SportsTennis,
                    contentDescription = null,
                    tint = NavyDeep,
                    modifier = Modifier.size(40.dp)
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = "Welcome to Pickleball App",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Your Court, Your Game,\nYour Community",
                fontFamily = NotoSans,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = TextSecondaryLight,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))

            PrimaryButton(
                text = "Get Started",
                onClick = onGetStarted
            )

            Spacer(Modifier.height(16.dp))

            Row {
                Text(
                    text = "Already have an account? ",
                    color = TextSecondaryLight
                )
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignIn() },
                    color = PrimaryGreen
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) {
                    Box(
                        Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onBackground.copy(0.3f))
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OnboardingScreensPreview() {
    PickleballTheme {
        Surface {
            OnboardingScreens(
                onGetStarted = {},
                onSignIn = {}
            )
        }
    }
}

