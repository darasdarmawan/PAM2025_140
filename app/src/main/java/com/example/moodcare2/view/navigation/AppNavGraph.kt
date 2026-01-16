package com.example.moodcare2.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moodcare2.utils.SessionManager
import com.example.moodcare2.view.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddMood : Screen("add_mood")
    object MoodHistory : Screen("mood_history")
    object DetailMood : Screen("detail_mood")
    object EditMood : Screen("edit_mood")
    object MoodGraph : Screen("mood_graph")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    sessionManager: SessionManager
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                sessionManager = sessionManager
            )
        }

        // Authentication
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        // Main App
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Mood Management
        composable(Screen.AddMood.route) {
            AddMoodScreen(navController = navController)
        }

        composable(Screen.MoodHistory.route) {
            MoodHistoryScreen(navController = navController)
        }

        composable(
            route = "${Screen.DetailMood.route}/{moodId}",
            arguments = listOf(navArgument("moodId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getInt("moodId") ?: 0
            DetailMoodScreen(navController = navController, moodId = moodId)
        }

        composable(
            route = "${Screen.EditMood.route}/{moodId}",
            arguments = listOf(navArgument("moodId") { type = NavType.IntType })
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getInt("moodId") ?: 0
            EditMoodScreen(navController = navController, moodId = moodId)
        }

        // Graph
        composable(Screen.MoodGraph.route) {
            MoodGraphScreen(navController = navController)
        }

        // Profile
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
    }
}