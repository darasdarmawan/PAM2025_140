package com.example.moodcare2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.R
import com.example.moodcare2.utils.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    sessionManager: SessionManager
) {
    LaunchedEffect(Unit) {
        delay(2500)

        val destination = if (sessionManager.isLoggedIn()) {
            "home"
        } else {
            "login"
        }

        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.PinkPrimary),
                        colorResource(R.color.PinkSecondary).copy(alpha = 0.8f),
                        colorResource(R.color.Matcha)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(160.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f) // Efek glassmorphism di belakang logo
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.fox),
                        contentDescription = "Fox Logo",
                        modifier = Modifier.size(110.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "MoodCare",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )

            Text(
                text = "Track Your Mood with Love 💕",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Version 2.0",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}