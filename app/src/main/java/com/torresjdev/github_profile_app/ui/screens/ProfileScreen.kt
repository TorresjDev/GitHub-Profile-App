package com.torresjdev.github_profile_app.ui.screens

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.torresjdev.github_profile_app.R
import com.torresjdev.github_profile_app.model.GitHubProfile
import com.torresjdev.github_profile_app.model.GitHubRepo
import com.torresjdev.github_profile_app.ui.GitHubUiState

/**
 * GitHub Profile Screen
 * @param uiState The current state of the UI
 * @param searchQuery The current search query
 * @param onSearchQueryChange Callback to update the search query
 * @param onSearch Callback to trigger the search action
 * @param onRetry Callback to retry the search action
 * @param isDarkMode Whether the UI is in dark mode
 * @param onToggleDarkMode Callback to toggle the dark mode
 * @param modifier Modifier for styling
 */
@Composable
fun ProfileScreen(
    uiState: GitHubUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top Bar: GitHub Icon | Search Bar | Theme Toggle
        ProfileTopBar(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearch = onSearch,
            isDarkMode = isDarkMode,
            onToggleDarkMode = onToggleDarkMode
        )

        // Content with animation
        Column(modifier = Modifier.weight(1f)) {
            AnimatedVisibility(
                visible = uiState is GitHubUiState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingContent()
            }

            AnimatedVisibility(
                visible = uiState is GitHubUiState.Success,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (uiState is GitHubUiState.Success) {
                    ProfileContent(
                        profile = uiState.profile,
                        repos = uiState.repos
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState is GitHubUiState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ErrorContent(onRetry = onRetry)
            }
        }
    }
}

/**
 * Top Bar with GitHub Icon, Search Bar, and Theme Toggle
 * @param searchQuery The current search query
 * @param onSearchQueryChange Callback to update the search query
 * @param onSearch Callback to trigger the search action
 * @param isDarkMode Whether the UI is in dark mode
 * @param onToggleDarkMode Callback to toggle the dark mode
 * @param modifier Modifier for styling
 */
@Composable
private fun ProfileTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // GitHub Icon
        Image(
            painter = painterResource(id = R.drawable.github_logo),
            contentDescription = "GitHub",
            modifier = Modifier.size(36.dp),
            colorFilter = ColorFilter.tint(
                if (isDarkMode) Color.White else Color.Black
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search GitHub user...") },
            singleLine = true,
            shape = RoundedCornerShape(26.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    if (searchQuery.isNotBlank()) {
                        onSearch()
                    }
                }
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Theme Toggle
        IconButton(onClick = onToggleDarkMode) {
            Icon(
                painter = painterResource(
                    id = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                ),
                contentDescription = "Toggle theme",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

/**
 * Loading Content Animation for loading state of the UI.
 * @param modifier Modifier for styling
 * @receiver Modifier for styling
 */
@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Error Content Animation for error state of the UI.
 * @param onRetry Callback to retry the search action
 * @param modifier Modifier for styling
 * @receiver Modifier for styling
 */
@Composable
private fun ErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load profile",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please check the username and try again",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

/**
 * Profile Content for success state of the UI.
 * @param profile The GitHub profile
 * @param repos The list of GitHub repositories
 * @param modifier Modifier for styling
 * @receiver Modifier for styling
 */
@Composable
private fun ProfileContent(
    profile: GitHubProfile,
    repos: List<GitHubRepo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Profile Header
        item {
            ProfileHeader(profile)
        }

        // Repositories Section Title
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Public Repositories (${profile.publicRepos})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Repository List
        items(repos) { repo ->
            RepoCard(repo)
        }
    }
}

/**
 * Profile Header for the GitHub profile.
 * @param profile The GitHub profile
 */
@Composable
private fun ProfileHeader(profile: GitHubProfile) {
    /**
     * GitHub Profile Card: Profile Picture, Details, Stats
     */
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // GitHub Profile Picture
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(profile.avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // GitHub Profile Details
            Text(
                text = profile.name ?: profile.login,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "@${profile.login}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (profile.bio != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * GitHub Profile Stats: Followers, Following, Repos
             */
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatItem(label = "Followers", value = profile.followers.toString())
                StatItem(label = "Following", value = profile.following.toString())
                StatItem(label = "Repos", value = profile.publicRepos.toString())
            }
        }
    }
}

/**
 * Stat Item for the GitHub profile stats.
 * @param label The label for the stat
 * @param value The value for the stat
 */
@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Repository Card for the GitHub repository.
 * @param repo The GitHub repository
 */
@Composable
private fun RepoCard(repo: GitHubRepo) {
    val context = LocalContext.current

    /**
     * GitHub Repository Card: Name, Description, Language, Stars
     */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, repo.htmlUrl.toUri())
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            if (repo.description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = repo.language ?: "Unknown",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "⭐ ${repo.stargazersCount}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
