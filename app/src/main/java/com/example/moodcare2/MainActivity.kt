package com.example.moodcare2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.moodcare2.ui.theme.Moodcare2Theme
import com.example.moodcare2.view.navigation.NavGraph
import com.example.moodcare2.view.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Moodcare2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodCareApp()
                }
            }
        }
    }
}

@Composable
fun MoodCareApp() {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val sessionManager = application.sessionManager

    val navController = rememberNavController()

    NavGraph(
        navController = navController,
        startDestination = Screen.Splash.route,
        sessionManager = sessionManager
    )
}