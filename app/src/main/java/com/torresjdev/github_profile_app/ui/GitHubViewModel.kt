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

sealed interface GitHubUiState {
    data class Success(val profile: GitHubProfile, val repos: List<GitHubRepo>) : GitHubUiState
    object Error : GitHubUiState
    object Loading : GitHubUiState
}

class GitHubViewModel : ViewModel() {
    // Manual dependency injection
    private val repository: GitHubRepository = NetworkGitHubRepository()

    private val _uiState = MutableStateFlow<GitHubUiState>(GitHubUiState.Loading)
    val uiState: StateFlow<GitHubUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchUser() {
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            getGitHubData(query)
        }
    }

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
