package com.bptracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bptracker.presentation.home.HomeViewModel
import com.bptracker.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object History : Screen("history")
    object Insights : Screen("insights")
    object Settings : Screen("settings")
}

@Composable
fun BPTrackerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToInsights = { navController.navigate(Screen.Insights.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Other screens will be added later
        composable(Screen.Camera.route) {
            // Camera screen placeholder
        }

        composable(Screen.History.route) {
            // History screen placeholder
        }

        composable(Screen.Insights.route) {
            // Insights screen placeholder
        }

        composable(Screen.Settings.route) {
            // Settings screen placeholder
        }
    }
}
