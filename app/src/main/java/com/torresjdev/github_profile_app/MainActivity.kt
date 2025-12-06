package com.torresjdev.github_profile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.torresjdev.github_profile_app.ui.GitHubViewModel
import com.torresjdev.github_profile_app.ui.screens.ProfileScreen
import com.torresjdev.github_profile_app.ui.screens.SearchScreen
import com.torresjdev.github_profile_app.ui.screens.SplashScreen
import com.torresjdev.github_profile_app.ui.theme.GitHubProfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GitHubViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            /**
             * GitHubProfileAppTheme is a custom theme that is used to style the UI of the app.
             * It takes a darkTheme parameter that is used to determine whether the app should use a dark or light theme.
             */
            GitHubProfileAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                val uiState by viewModel.uiState.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()

                /**
                 * Scaffold is a composable that provides a basic structure for other composables.
                 * It takes a modifier parameter that is used to modify the layout of the UI.
                 */
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        /**
                         * SplashScreen is a composable that is used to display a splash screen while the app is loading.
                         */
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("search") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }
                        /**
                         * SearchScreen is a composable that is used to search for a GitHub user.
                         */
                        composable("search") {
                            SearchScreen(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                onSearch = {
                                    viewModel.searchUser()
                                    navController.navigate("profile")
                                },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { viewModel.toggleDarkMode() }
                            )
                        }
                        /**
                         * ProfileScreen is a composable that is used to display the profile of a GitHub user.
                         */
                        composable("profile") {
                            ProfileScreen(
                                uiState = uiState,
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                onSearch = { viewModel.searchUser() },
                                onRetry = { viewModel.searchUser() },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { viewModel.toggleDarkMode() }
                            )
                        }
                    }
                }
            }
        }
    }
}