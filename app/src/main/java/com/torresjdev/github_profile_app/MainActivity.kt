package com.torresjdev.github_profile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.torresjdev.github_profile_app.ui.GitHubViewModel
import com.torresjdev.github_profile_app.ui.HomeScreen
import com.torresjdev.github_profile_app.ui.theme.GitHubProfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubProfileAppTheme {
                val viewModel: GitHubViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.getGitHubData("torresjdev")
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        gitHubUiState = uiState,
                        retryAction = { viewModel.getGitHubData("torresjdev") },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}