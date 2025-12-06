package com.torresjdev.github_profile_app.data

import com.torresjdev.github_profile_app.model.GitHubProfile
import com.torresjdev.github_profile_app.model.GitHubRepo
import com.torresjdev.github_profile_app.network.GitHubApi

/**
 * GitHub repository interface
 * Used to fetch data from the network
 * @constructor Create empty GitHub repository
 * @property getUserProfile Gets the user profile from the network
 * @property getUserRepos Gets the user repositories from the network
 */
interface GitHubRepository {
    suspend fun getUserProfile(username: String): GitHubProfile
    suspend fun getUserRepos(username: String): List<GitHubRepo>
}

/**
 * Network GitHub repository
 * @constructor Create empty Network GitHub repository
 * @property getUserProfile Gets the user profile from the network
 * @property getUserRepos Gets the user repositories from the network
 */
class NetworkGitHubRepository : GitHubRepository {
    override suspend fun getUserProfile(username: String): GitHubProfile {
        return GitHubApi.retrofitService.getUser(username)
    }

    override suspend fun getUserRepos(username: String): List<GitHubRepo> {
        return GitHubApi.retrofitService.getRepos(username)
    }
}
