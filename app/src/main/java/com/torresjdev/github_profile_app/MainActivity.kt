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
import com.torresjdev.github_profile_app.ui.theme.GitHubProfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubProfileAppTheme {
                val navController = rememberNavController()
                val viewModel: GitHubViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "search",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("search") {
                            SearchScreen(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                onSearch = {
                                    viewModel.searchUser()
                                    navController.navigate("profile")
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                uiState = uiState,
                                onBackClick = { navController.popBackStack() },
                                onRetry = { viewModel.searchUser() }
                            )
                        }
                    }
                }
            }
        }
    }
}