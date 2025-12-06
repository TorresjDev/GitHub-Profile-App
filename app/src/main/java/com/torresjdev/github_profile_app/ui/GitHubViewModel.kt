package com.torresjdev.github_profile_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.torresjdev.github_profile_app.data.GitHubRepository
import com.torresjdev.github_profile_app.data.NetworkGitHubRepository
import com.torresjdev.github_profile_app.model.GitHubProfile
import com.torresjdev.github_profile_app.model.GitHubRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

/**
 * Represents the state of the GitHub UI.
 * It can be either loading, showing data, or showing an error.
 * @property GitHubUiState.Loading: The UI is currently loading data.
 * @property GitHubUiState.Success: The UI has successfully loaded data.
 * @property GitHubUiState.Error: An error occurred while loading data.
 * @constructor Creates a new instance of GitHubUiState.
 *
 */
sealed interface GitHubUiState {
    data class Success(val profile: GitHubProfile, val repos: List<GitHubRepo>) : GitHubUiState
    object Error : GitHubUiState
    object Loading : GitHubUiState
}

/**
 * Enum representing the available sorting options for repositories.
 * Used to control the display order of repositories in the profile view.
 */
enum class RepoSortOption {
    /** Sort by star count (highest first) */
    STARS,
    /** Sort by last update date (most recent first) */
    RECENT,
    /** Sort alphabetically by repository name (A-Z) */
    NAME
}

/**
 * Represents the view model for the GitHub UI.
 * It is responsible for managing the data and state for the UI.
 * @constructor Creates a new instance of GitHubViewModel.
 */
class GitHubViewModel : ViewModel() {
    // Manual dependency injection
    private val repository: GitHubRepository = NetworkGitHubRepository()

    private val _uiState = MutableStateFlow<GitHubUiState>(GitHubUiState.Loading)
    val uiState: StateFlow<GitHubUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Repository sort option state (default: sort by stars)
    private val _sortOption = MutableStateFlow(RepoSortOption.STARS)
    val sortOption: StateFlow<RepoSortOption> = _sortOption.asStateFlow()

    // Toggle dark mode
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Update search query
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Updates the repository sort option.
     * @param option The new sort option to apply.
     */
    fun updateSortOption(option: RepoSortOption) {
        _sortOption.value = option
    }

    // Search for user
    fun searchUser() {
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            getGitHubData(query)
        }
    }

    // Fetch GitHub data
    fun getGitHubData(username: String) {
        viewModelScope.launch {
            _uiState.value = GitHubUiState.Loading
            _uiState.value = try {
                val profile = repository.getUserProfile(username)
                val repos = repository.getUserRepos(username)
                GitHubUiState.Success(profile, repos)
            } catch (e: IOException) {
                GitHubUiState.Error
            } catch (e: HttpException) {
                GitHubUiState.Error
            }
        }
    }
}
