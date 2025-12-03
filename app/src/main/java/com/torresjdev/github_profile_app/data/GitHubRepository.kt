package com.torresjdev.github_profile_app.data

import com.torresjdev.github_profile_app.model.GitHubProfile
import com.torresjdev.github_profile_app.model.GitHubRepo
import com.torresjdev.github_profile_app.network.GitHubApi

interface GitHubRepository {
    suspend fun getUserProfile(username: String): GitHubProfile
    suspend fun getUserRepos(username: String): List<GitHubRepo>
}

class NetworkGitHubRepository : GitHubRepository {
    override suspend fun getUserProfile(username: String): GitHubProfile {
        return GitHubApi.retrofitService.getUser(username)
    }

    override suspend fun getUserRepos(username: String): List<GitHubRepo> {
        return GitHubApi.retrofitService.getRepos(username)
    }
}
